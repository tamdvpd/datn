package com.fashionstore.fashionstore.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Path ROOT = Paths.get("upload/images/avatars");
    private static final Set<String> ALLOWED = Set.of("image/jpeg", "image/png", "image/webp", "image/jpg");
    private static final long MAX_SIZE = 5L * 1024 * 1024; // 5MB

    public String saveAvatar(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty())
            throw new IllegalArgumentException("File rỗng");
        if (file.getSize() > MAX_SIZE)
            throw new IllegalArgumentException("File quá lớn (>5MB)");
        if (!ALLOWED.contains(file.getContentType()))
            throw new IllegalArgumentException("Chỉ cho phép JPEG/PNG/WEBP");

        if (!Files.exists(ROOT))
            Files.createDirectories(ROOT);

        String ext = getExt(file.getOriginalFilename());
        String filename = "avatar_" + System.currentTimeMillis() + "_" + UUID.randomUUID()
                + (ext.isEmpty() ? "" : "." + ext);
        Path dest = ROOT.resolve(filename).normalize().toAbsolutePath();

        if (!dest.startsWith(ROOT.toAbsolutePath().normalize())) {
            throw new SecurityException("Đường dẫn không hợp lệ");
        }

        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
        return "/images/avatars/" + filename; // trả về path tương đối để FE ghép host
    }

    public void deleteAvatarIfExists(String urlPath) {
        if (urlPath == null || urlPath.isBlank())
            return;
        int idx = urlPath.lastIndexOf('/');
        if (idx < 0)
            return;
        String filename = urlPath.substring(idx + 1);
        Path p = ROOT.resolve(filename).normalize().toAbsolutePath();
        try {
            Files.deleteIfExists(p);
        } catch (IOException ignored) {
        }
    }

    private String getExt(String name) {
        if (name == null)
            return "";
        int dot = name.lastIndexOf('.');
        return dot >= 0 ? name.substring(dot + 1).toLowerCase() : "";
    }
}

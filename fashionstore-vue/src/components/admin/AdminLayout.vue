<template>
    <div class="admin-layout">
        <MainHeader />

        <div class="content">
            <router-view />
        </div>

        <MainFooter />
    </div>
</template>

<script>
import MainHeader from "./MainHeader.vue";
import MainFooter from "./MainFooter.vue";

export default {
    components: {
        MainHeader,
        MainFooter,
    },
};
</script>

<style scoped>
.admin-layout {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
}

.content {
    flex: 1;
    padding: 20px;
}
</style>

<template>
  <div class="dashboard-container">
    <div class="dashboard-text">name: {{ name }}</div>

    <AvatarUpload></AvatarUpload>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import AvatarUpload from '@/components/Upload/AvatarUpload'

export default {
  name: 'Dashboard',
  components:{"AvatarUpload":AvatarUpload},
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>

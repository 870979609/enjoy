<template>
  <div class="singleImageUpload2 upload-container">
    <el-upload
      :data="dataObj"
      :multiple="false"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      class="image-uploader"
      drag
      :action="baseURL + '/commonapi/uploadfile'"
      :before-upload="beforeAvatarUpload"
    >
      <img v-if="imageUrl" :src="imageUrl" class="avatar" />
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>

    </el-upload>
  </div>
</template>

<script>
  export default {
    props: {
      value: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        baseURL: this.$baseFileUrlPrefix,
        imageUrl: "",
        dataObj:{}
      };
    },
    computed: {},
    methods: {
      handleAvatarSuccess(res, file) {
        this.imageUrl = res;

        console.log(this.imageUrl);
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === "image/jpeg";
        const isLt2M = file.size / 1024 / 1024 < 5;

        if (!isJPG) {
          this.$message.error("上传头像图片只能是 PNG 格式!");
        }
        if (!isLt2M) {
          this.$message.error("上传头像图片大小不能超过 5MB!");
        }
        return isJPG && isLt2M;
      }
    }
  }
</script>

<style lang="scss" scoped>
  .upload-container {
    width: 100%;
    height: 100%;
    position: relative;
    .image-uploader {
      height: 100%;
    }
    .image-preview {
      width: 100%;
      height: 100%;
      position: absolute;
      left: 0px;
      top: 0px;
      border: 1px dashed #d9d9d9;
      .image-preview-wrapper {
        position: relative;
        width: 100%;
        height: 100%;
        img {
          width: 100%;
          height: 100%;
        }
      }
      .image-preview-action {
        position: absolute;
        width: 100%;
        height: 100%;
        left: 0;
        top: 0;
        cursor: default;
        text-align: center;
        color: #fff;
        opacity: 0;
        font-size: 20px;
        background-color: rgba(0, 0, 0, .5);
        transition: opacity .3s;
        cursor: pointer;
        text-align: center;
        line-height: 200px;
        .el-icon-delete {
          font-size: 36px;
        }
      }
      &:hover {
        .image-preview-action {
          opacity: 1;
        }
      }
    }
  }
</style>

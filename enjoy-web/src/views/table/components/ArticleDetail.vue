<template>
  <div class="createPost-container">
    <el-form ref="postForm" :model="postForm" :rules="rules" class="form-container">

      <sticky :z-index="10" :class-name="'sub-navbar '+postForm.status">
        <el-button v-loading="loading" style="margin-rignt: 10px;float: right" type="success" @click="submitForm">
          Publish
        </el-button>
        <el-button v-loading="loading" style="margin-rignt: 10px;float: right" type="warning" @click="draftForm">
          Draft
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-row>
          <el-col :span="24">
            <el-form-item style="margin-bottom: 40px;" prop="title">
              <MDinput v-model="postForm.title" :maxlength="100" name="name" required>
                Title
              </MDinput>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item prop="summary" style="margin-bottom: 40px; margin-right: 50px;" label-width="80px" label="摘要:">
          <el-input v-model="postForm.summary" :rows="1" type="textarea" class="article-textarea" autosize
                    placeholder="Please enter the content"/>
          <span v-show="contentShortLength" class="word-counter">{{ contentShortLength }}words</span>
        </el-form-item>

        <el-form-item prop="content" style="margin-bottom: 30px;">
          <Tinymce ref="editor" v-model="postForm.content" :height="400"/>
        </el-form-item>

        <div class="postInfo-container">
          <el-row>
            <el-col :span="12">
              <el-form-item label-width="80px" label="作者:" class="postInfo-container-item" v-show="!isOwn">
                <el-input v-model="postForm.author" :rows="1" type="text" placeholder="作者"/>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label-width="100px" label="创建时间:" class="postInfo-container-item" v-show="isEdit">
                <el-date-picker v-model="createTime" type="datetime" format="yyyy-MM-dd HH:mm:ss"
                                placeholder="Select date and time" readonly/>
              </el-form-item>
            </el-col>

          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item prop="category" label-width="100px" label="分类:" class="postInfo-container-item">
                <el-select v-model="postForm.categorys" style="width: 100%" multiple filterable allow-create>
                  <el-option v-for="(item) in ArticleCategoryOptions" :key="item.name" :label="item.name"
                             :value="item.id"/>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label-width="100px" label="标签:" class="postInfo-container-item">
                <el-select v-model="postForm.tags" style="width: 100%" multiple>
                  <el-option v-for="(item) in ArticleTagOptions" :key="item.name" :label="item.name"
                             :value="item.id"/>
                </el-select>
              </el-form-item>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item prop="origin" label-width="100px" label="文章来源:" class="postInfo-container-item">
                <el-select v-model="postForm.origin" clearable placeholder="请选择">
                  <el-option v-for="(item) in ArticleOriginOptions" :key="item.content" :label="item.content"
                             :value="item.code"/>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item prop="visibility" label-width="100px" label="可见性:" class="postInfo-container-item">
                <el-select v-model="postForm.visibility" clearable placeholder="请选择">
                  <el-option v-for="(item) in ArticleVisibilityOptions" :key="item.content" :label="item.content"
                             :value="item.code"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label-width="120px" label="文章状态:" class="postInfo-container-item">
                <el-select v-model="postForm.status">
                  <el-option v-for="(item) in ArticleStatusOptions" :key="item.content" :label="item.content"
                             :value="item.code"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label-width="120px" label="文章状态:" class="postInfo-container-item">
                <CodeDropDown v-model="postForm.status" :is_disabled="false" dmbh="articlestatus"></CodeDropDown>
              </el-form-item>
            </el-col>

          </el-row>
        </div>
      </div>
    </el-form>
  </div>
</template>

<script>
  import Tinymce from '@/components/Tinymce'
  import MDinput from '@/components/MDinput'
  import CodeDropDown from '@/components/CodeDropDown'
  import Sticky from '@/components/Sticky' // 粘性header组件
  import {validURL} from '@/utils/validate'
  import {fetchArticle, createArticle, getMyCategorysAndTags} from '@/api/article'
  import {getCodeByDmbh} from '@/api/common'

  const defaultForm = {
    id: undefined,
    title: '', // 文章题目
    userid: '',
    summary: '',
    categorys: [],
    tags: [],
    origin: [],
    visibility: [],
    status: [],
    is_top: false,
    content: '', // 文章内容
    praise_num: 0,
    collection_num: 0,
    create_time: undefined,
    update_time: undefined
  }

  export default {
    name: 'ArticleDetail',
    components: {CodeDropDown, Tinymce, MDinput, Sticky},
    props: {
      isEdit: {
        type: Boolean,
        default: false
      },
      isOwn: {
        type: Boolean,
        default: false
      }
    },
    data() {
      const validateRequire = (rule, value, callback) => {
        if (value === '') {
          this.$message({
            message: rule.field + '为必填项',
            type: 'error'
          })
          callback(new Error(rule.field + '为必填项'))
        } else {
          callback()
        }
      }
      return {
        postForm: Object.assign({}, defaultForm),
        loading: false,
        categorys: [],
        tags: [],
        ArticleOriginOptions: [],
        ArticleVisibilityOptions: [],
        ArticleStatusOptions: [],
        ArticleCategoryOptions: [],
        ArticleTagOptions: [],
        rules: {
          title: [{required: true, message: '请输入文章标题', trigger: 'blur'}],
          summary: [{required: true, message: '请输入文章摘要', trigger: 'blur'}],
          origin: [{required: true, message: '请设置文章来源', trigger: 'blur'}],
          visibility: [{required: true, message: '请设置文章可见性', trigger: 'blur'}]
        },
        tempRoute: {}
      }
    },
    computed: {
      contentShortLength() {
        return this.postForm.summary.length
      },
      createTime: {
        // set and get is useful when the data
        // returned by the back end api is different from the front end
        // back end return => "2013-06-25 06:59:25"
        // front end need timestamp => 1372114765000
        get() {
          return (+new Date(this.postForm.create_time))
        },
        set(val) {
          this.postForm.create_time = new Date(val)
        }
      }
    },
    created() {
      this.getCodeInfo();
      this.getMyCategorysAndTags();
      if (this.isEdit) {
        const id = this.$route.params && this.$route.params.id;
        this.fetchData(id);
      }

      // Why need to make a copy of this.$route here?
      // Because if you enter this page and quickly switch tag, may be in the execution of the setTagsViewTitle function, this.$route is no longer pointing to the current page
      // https://github.com/PanJiaChen/vue-element-admin/issues/1221
      this.tempRoute = Object.assign({}, this.$route)
    },
    methods: {
      fetchData(id) {
        fetchArticle(id).then(response => {
          this.postForm = response.data

          // just for test
          this.postForm.title += `   Article Id:${this.postForm.id}`
          this.postForm.content_short += `   Article Id:${this.postForm.id}`

          // set tagsview title
          //this.setTagsViewTitle()

          // set page title
          this.setPageTitle()
        }).catch(err => {
          this.$message({
            message: err,
            type: 'error'
          });
        })
      },
      /*    setTagsViewTitle() {
            const title = 'Edit Article'
            const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.postForm.id}` })
            this.$store.dispatch('tagsView/updateVisitedView', route)
          },*/
      setPageTitle() {
        const title = 'Edit Article'
        document.title = `${title} - ${this.postForm.id}`
      },
      submitForm() {
        console.log(this.postForm)
        this.$refs.postForm.validate(valid => {
          if (valid) {
            this.loading = true

            createArticle(this.postForm).then(response => {

              this.$notify({
                title: '成功',
                message: '发布文章成功',
                type: 'success',
                duration: 2000
              })
            }).catch(err => {
              this.$message({
                message: err,
                type: 'error'
              });
            });
          //  this.postForm.status = 'published'
            this.loading = false
          } else {
            this.$message({
              message: '请填写必填项',
              type: 'warning'
            });
            return false;
          }
        })
      },
      draftForm() {
        if (this.postForm.title.length === 0) {
          this.$message({
            message: '请填写必要的标题',
            type: 'warning'
          })
          return
        }
        this.$message({
          message: '保存成功',
          type: 'success',
          showClose: true,
          duration: 1000
        })
        this.postForm.status = '';
      },
      getCodeInfo() {
        getCodeByDmbh("articleorigin,articlevisibility,articlestatus").then(response => {
          if (!response.data) {
            return;
          }

          this.ArticleOriginOptions = response.data["articleorigin"];
          this.ArticleVisibilityOptions = response.data["articlevisibility"];
          this.ArticleStatusOptions = response.data["articlestatus"];
        })
      },
      getMyCategorysAndTags() {
        getMyCategorysAndTags().then(response => {
          if (!response.data) {
            return;
          }

          this.ArticleCategoryOptions = response.data["categorys"];
          this.ArticleTagOptions = response.data["tags"];
        });
      }
    }
  }
</script>

<style lang="scss" scoped>

  .createPost-container {
    position: relative;

    .createPost-main-container {
      padding: 20px 20px 10px 25px;

      .postInfo-container {
        position: relative;
        margin-bottom: 10px;

        .postInfo-container-item {
          float: left;
          width: 100%;
        }
      }
    }

    .word-counter {
      width: 40px;
      position: absolute;
      right: 10px;
      top: 0px;
    }
  }

  .article-textarea {
    textarea {
      padding-right: 40px;
      resize: none;
      border: none;
      border-radius: 0px;
      border-bottom: 1px solid #bfcbd9;
    }
  }
</style>

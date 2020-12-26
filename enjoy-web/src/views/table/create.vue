<template>
  <article-detail :is-edit="false" :is-own="true" />
</template>

<script>
import ArticleDetail from './components/ArticleDetail'

export default {
  name: 'CreateArticle',
  components: { ArticleDetail }
}
</script>


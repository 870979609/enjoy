<template>
  <el-select :disabled="is_disabled">
    <el-option v-for="(item) in codeList" :key="item.content" :label="item.content"
               :value="item.code"/>
  </el-select>
</template>

<script>
  import {getCodeByDmbh} from '@/api/common'

    export default {
        name: "codeDropDown",
        props:["value", "dmbh", "is_disabled"],
      data() {
          return {
            codeList: []
          }
      },

      created() {
        this.getCodeInfo();
      },

      methods:{
        getCodeInfo() {
          getCodeByDmbh(this.dmbh).then(response => {
            if (!response.data) {
              return;
            }
            this.codeList = response.data[this.dmbh];
          })
        }
      }

    }
</script>


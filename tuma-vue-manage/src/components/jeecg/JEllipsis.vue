<template>
  <a-tooltip v-if="(_value.length / 2) > length" placement="topLeft">
    <template slot="title">
      <span>{{ value }}</span>
    </template>
    {{ value | ellipsis(length) }}
  </a-tooltip>
  <span v-else>{{ value }}</span>
</template>

<script>
export default {
  name: "JEllipsis",

  props: {
    value: {
      type: String,
      required: false,
      default: "--"
    },
    length: {
      type: Number,
      required: false,
      default: 25
    }
  },

  computed: {
    _value() {
      return this.value.replace (/[^\x00-\xff]/g,"rr")
    }
  }
};
</script>

<style scoped>
</style>

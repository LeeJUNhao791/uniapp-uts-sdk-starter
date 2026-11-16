<template>
  <view class="screen">
    <text class="heading">Android AAR 文本校验</text>
    <input v-model="input" class="input" placeholder="输入待校验文本" />
    <button @click="check">校验文本</button>
    <button @click="clearInput">清空输入</button>
    <text>{{ status }}</text>
  </view>
</template>

<script setup>
import { ref } from 'vue'
// #ifdef APP-PLUS
import { checkText } from '@/uni_modules/ljh-sdk-starter'
// #endif

const input = ref('hello')
const status = ref('等待校验')

function check() {
  // #ifdef APP-PLUS
  const result = checkText(input.value)
  status.value = result.ok ? `长度 ${result.length}` : `错误 ${result.errorCode}`
  // #endif
  // #ifndef APP-PLUS
  status.value = '请在 Android App 中运行此示例'
  // #endif
}

function clearInput() {
  input.value = ''
  status.value = '等待校验'
}
</script>

<style scoped>
.screen {
  padding: 48rpx;
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}
.heading {
  font-size: 42rpx;
  font-weight: bold;
}
.input {
  padding: 18rpx 24rpx;
  border: 1px solid #aaa;
}
</style>

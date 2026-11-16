<template>
  <view class="screen">
    <text class="heading">Android SDK 回调示例</text>
    <text>模拟扫码，无需扫码设备。</text>
    <view class="actions">
      <button :disabled="listening" @click="start">开始监听</button>
      <button @click="simulate">模拟扫码</button>
      <button @click="simulateDelayed">延时模拟扫码</button>
      <button :disabled="!listening" @click="stop">停止监听</button>
    </view>
    <text>收到 {{ count }} 条回调</text>
    <text v-if="lastCode">最近条码 {{ lastCode }}</text>
    <text>{{ status }}</text>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onHide, onUnload } from '@dcloudio/uni-app'
// #ifdef APP-PLUS
import { onScan, simulateScan, simulateDelayedScan, stopScan } from '@/uni_modules/ljh-sdk-starter'
// #endif

const listening = ref(false)
const count = ref(0)
const lastCode = ref('')
const status = ref('等待开始监听')
let subscriptionId = 0

function start() {
  // #ifdef APP-PLUS
  if (listening.value) return
  const subscription = ++subscriptionId
  listening.value = onScan(code => {
    if (!listening.value || subscription !== subscriptionId) return
    count.value += 1
    lastCode.value = code
    status.value = '收到原生 SDK 回调'
  })
  status.value = listening.value ? '已开始监听' : '已有监听，未重复注册'
  // #endif
  // #ifndef APP-PLUS
  status.value = '请在 Android App 中运行此示例'
  // #endif
}

function simulate() {
  // #ifdef APP-PLUS
  if (!simulateScan()) status.value = '当前未监听，没有收到回调'
  // #endif
  // #ifndef APP-PLUS
  status.value = '请在 Android App 中运行此示例'
  // #endif
}

function simulateDelayed() {
  // #ifdef APP-PLUS
  status.value = simulateDelayedScan() ? '等待原生 SDK 回调' : '当前未监听，没有收到回调'
  // #endif
  // #ifndef APP-PLUS
  status.value = '请在 Android App 中运行此示例'
  // #endif
}

function stop() {
  if (!listening.value) return
  listening.value = false
  // #ifdef APP-PLUS
  stopScan()
  // #endif
  status.value = '已停止监听'
}

onHide(stop)
onUnload(stop)
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
.actions {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}
.actions button {
  width: 100%;
}
</style>

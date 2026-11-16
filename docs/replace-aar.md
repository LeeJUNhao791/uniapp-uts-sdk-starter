# 在 uni-app 中替换 Android AAR

本例把扫码 AAR 换成独立的文本校验 AAR。Java 入口变为 `TextChecker.check(text)`，返回包含长度和错误码的对象。UTS 把它转换成页面可读的 `{ ok, length, errorCode }`。

## 1. 在干净副本中构建

新克隆一份仓库，后续操作都在这个副本中执行。按 [README 的环境说明](../README.md#1-构建和验证示例-aar)准备 JDK 17 和 Android SDK Platform 35。从仓库根目录运行下面的命令。

Windows PowerShell

```powershell
.\android-sdk\gradlew.bat -p android-sdk stageDemoAar :text-check:verifyTextCheck :text-check:assembleRelease
```

macOS / Linux

```sh
./android-sdk/gradlew -p android-sdk stageDemoAar :text-check:verifyTextCheck :text-check:assembleRelease
```

看到 `PASS TextChecker success, empty input and UTF-16 length` 后，确认原扫码 AAR 已进入插件的 `libs`，文本校验 AAR 生成于 `android-sdk/text-check/build/outputs/aar/text-check-release.aar`。

## 2. 替换依赖和调用

移除副本中 `demo/uni_modules/ljh-sdk-starter/utssdk/app-android/libs/demo-scanner-release.aar`，把 `text-check-release.aar` 放入同一目录，再把 [示例目录](../examples/text-check/demo)中的三个文件覆盖到 `demo` 下的相同位置。 [DCloud Android 原生配置](https://uniapp.dcloud.net.cn/plugin/uts-plugin.html#android%E5%B9%B3%E5%8F%B0%E5%8E%9F%E7%94%9F%E9%85%8D%E7%BD%AE)

| 示例文件 | 替换内容 |
| --- | --- |
| [interface.uts](../examples/text-check/demo/uni_modules/ljh-sdk-starter/utssdk/interface.uts) | 声明 `checkText(text)` 和页面可读的结果类型 |
| [app-android/index.uts](../examples/text-check/demo/uni_modules/ljh-sdk-starter/utssdk/app-android/index.uts) | 调用 Java 入口，把原生对象的字段转换成 UTS 结果 |
| [pages/index/index.vue](../examples/text-check/demo/pages/index/index.vue) | 传入文本，显示长度或错误码 |

Windows PowerShell

```powershell
$libs = 'demo/uni_modules/ljh-sdk-starter/utssdk/app-android/libs'
Remove-Item -LiteralPath (Join-Path $libs 'demo-scanner-release.aar')
Copy-Item -LiteralPath 'android-sdk/text-check/build/outputs/aar/text-check-release.aar' -Destination $libs
Copy-Item -Path 'examples/text-check/demo/*' -Destination 'demo' -Recurse -Force
```

macOS / Linux

```sh
libs=demo/uni_modules/ljh-sdk-starter/utssdk/app-android/libs
rm "$libs/demo-scanner-release.aar"
cp android-sdk/text-check/build/outputs/aar/text-check-release.aar "$libs/"
cp -R examples/text-check/demo/. demo/
```

## 3. 重新制作基座并在手机上验证

按 [Android 图文上手指南](android-quickstart.md)给副本配置测试 AppID，用独立的测试包名制作新的 Android 自定义调试基座。在 HBuilderX 里选择刚生成的本地基座运行。

| 手机操作 | 预期结果 |
| --- | --- |
| 保留默认输入 `hello`，点「校验文本」 | 页面显示 `长度 5` |
| 点「清空输入」，再点「校验文本」 | 页面显示 `错误 EMPTY_INPUT` |

本例长度按 Java 字符串的 UTF-16 单元计算，空格也计入长度。从自己的 SDK 接入时，按实际接口定义输入、结果和错误码。

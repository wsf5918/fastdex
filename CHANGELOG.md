## 0.0.2-beta1 (2017-3-14)

Bugfixes:

  - 修复动态生成的代码没有注入的问题

Features:

  - 支持自定义的compileJava任务

## 0.0.2-beta2 (2017-3-15)

Bugfixes:

  - 修复注入代码时仅注入默认构造方法的问题

## 0.0.2-rc1 (2017-3-16)

Bugfixes:

  - 修复buildType中有大写字母报错的问题
  - 修复没有注入app/build/generated/source/apt的bug
  - 修复获取依赖不完整的问题

Features:

  - 适配多个flavor的场景

## 0.0.2-rc5 (2017-3-19)

Bugfixes:

  - fix issue#2 解决在activity中getApplication()强转失败的问题
  - fix issue#3 windows路径不能盘符加:的问题

## 0.0.3-beta2 (2017-3-21)

Bugfixes:

   - 修改通过useCustomCompile关闭自定义编译任务后,造成检查环境不执行的问题
   - fix issue#4,编译报编码GBK的不可映射字符的问题

## 0.0.3-beta2 (2017-3-21)

Bugfixes:

   - fix issue#8,解决与tinkerpatch插件的冲突

Features:

   - 为提高稳定性,默认关闭掉自定义的compile任务,如果想使用增加了一个配置项useCustomCompile=true

   - 在重要节点添加日志方便以后排错

## 0.0.3-beta4 (2017-3-24)

Bugfixes:

   - fix issue#6,class name (*) does not match path (*)的问题
   - 解决全量打包后dex重复copy的问题





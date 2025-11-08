# 大师模式 (Master Mode) Compose 实现

## 概述

这是一个将 XML 布局完整转换为 Jetpack Compose 的商业级实现，采用了最精细的颗粒度划分，确保代码的可维护性和可扩展性。

## 文件结构

### 核心组件

1. **MasterModeScreen.kt** - 主界面，整合所有子组件
2. **MasterModeViewModel.kt** - 状态管理，使用 StateFlow 管理 UI 状态
3. **MasterModeColors.kt** - 颜色常量定义
4. **MasterModeDimensions.kt** - 尺寸常量定义
5. **MasterModeResources.kt** - 资源 ID 常量定义

### UI 组件

1. **ToolbarSection.kt** - Toolbar 区域（返回按钮、标题、下拉图标）
2. **CheckBoxBar.kt** - 复选框栏（纯音乐复选框、重置按钮）
3. **InputArea.kt** - 输入区域（纯音乐/非纯音乐两种模式）
4. **TagMenuSection.kt** - 标签菜单区域（音频参考、音调、风格标签）
5. **RememberStyleSection.kt** - 风格推荐区域
6. **ConfirmButtonSection.kt** - 确认按钮区域

## 功能特性

### 1. 纯音乐模式切换
- 支持纯音乐和非纯音乐两种输入模式
- 根据模式显示不同的输入界面
- 状态同步更新

### 2. 输入管理
- 纯音乐模式：歌曲名称输入 + 音乐风格添加
- 非纯音乐模式：歌曲名称 + 灵感内容输入
- 实时状态更新

### 3. 标签管理
- 音频参考标签（显示百分比）
- 音调标签
- 风格标签（支持图标）
- 标签删除功能

### 4. 风格推荐
- 显示推荐的音乐风格列表
- 支持点击添加风格

### 5. 确认按钮
- 根据输入状态启用/禁用
- 显示积分信息

## 需要的资源文件

请在 `app/src/main/res/drawable/` 目录下添加以下资源文件：

### Toolbar 资源
- `icon_back_arr_white.png` / `icon_back_arr_white.xml` - 返回按钮图标
- `icon_down_arr_white.png` / `icon_down_arr_white.xml` - 下拉箭头图标

### CheckBox Bar 资源
- `icon_master_mode_norm.png` / `icon_master_mode_norm.xml` - 纯音乐未选中状态图标
- `icon_master_mode_selected.png` / `icon_master_mode_selected.xml` - 纯音乐选中状态图标

### Tag 资源
- `icon_disable_close.png` / `icon_disable_close.xml` - 关闭图标
- `icon_del_black.png` / `icon_del_black.xml` - 删除图标
- `icon_add_black.png` / `icon_add_black.xml` - 添加图标
- `icon_tone.png` / `icon_tone.xml` - 音调图标（可选）

### Confirm Button 资源
- `icon_black_points.png` / `icon_black_points.xml` - 三个点图标

## 使用方法

```kotlin
@Composable
fun MyApp() {
    MasterModeScreen(
        viewModel = viewModel(),
        onBackClick = {
            // 处理返回事件
        }
    )
}
```

## 设计原则

1. **单一职责** - 每个组件只负责一个功能
2. **可复用性** - 组件高度可复用
3. **状态管理** - 使用 ViewModel + StateFlow 管理状态
4. **常量管理** - 颜色、尺寸、资源集中管理
5. **类型安全** - 使用 Kotlin 类型系统确保类型安全

## 扩展建议

1. **添加数据持久化** - 使用 Room 或 DataStore 保存用户输入
2. **添加网络请求** - 集成 Retrofit 获取风格推荐数据
3. **添加动画** - 使用 Compose Animation API 添加过渡动画
4. **添加单元测试** - 为 ViewModel 和组件添加测试
5. **添加 UI 测试** - 使用 Compose Test API 添加 UI 测试

## 注意事项

1. 确保所有资源文件已添加到项目中
2. 如果资源文件不存在，代码会编译失败，请先添加资源文件
3. 根据实际需求调整颜色、尺寸等常量
4. 根据业务逻辑实现 ViewModel 中的 TODO 部分

## 技术栈

- Jetpack Compose
- ViewModel + StateFlow
- Hilt (依赖注入)
- Material3

## 版本历史

- v1.0.0 - 初始版本，完整实现 XML 到 Compose 的转换



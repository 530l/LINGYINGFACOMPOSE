已根据 NavigationTemplate.kt 中的模式重构了 AppNavHost 和 MainActivity 的导航逻辑。
主要变更如下：
创建核心导航状态管理 (NavigationState.kt):
在 com.lyf.compose.core.nav3 包下创建了 NavigationState.kt。
实现了 NavigationState 类，用于管理多回退栈（TopLevelStack 和 SubStacks）。
实现了 rememberNavigationState Composable，用于初始化和保存导航状态。
实现了 MultiStackNavigator 类，实现了 Navigator 接口，处理多栈导航逻辑（包括 navigate, navigateRoot, pop）。
实现了 toEntries 扩展函数，将状态转换为 NavDisplay 可用的 NavEntry 列表，并自动应用 SaveableState 和 ViewModelStore 装饰器。
重构 AppNavHost.kt:
移除了原有的 rememberNavBackStack 和简单的 createNavigator 逻辑。
引入了 topLevelKeys 定义（Splash, Login, Home）。
使用 rememberNavigationState 初始化导航状态。
使用 MultiStackNavigator 作为导航控制器，并通过 LocalNavigator 提供给全局。
更新了 LaunchedEffect 中的登录态分流逻辑，使用 navigator.navigateRoot 切换根页面。
更新了 handleBack 逻辑，适配多栈结构，并保留了“双击返回退出”和“登录页返回退出”的逻辑。
更新了 NavDisplay 的使用方式，通过 navState.toEntries 提供页面入口。
MainActivity.kt:
保持不变，它只需调用 AppNavHost() 即可，内部实现已更新。
现在项目使用了更健壮的多栈导航架构，符合 androidx.navigation3 的最佳实践，
并且能够更好地处理复杂的导航场景（如 Tab 切换、根页面切换等）。




已根据 NavigationTemplate.kt 中的模式重构了 AppNavHost 和 MainActivity 的导航逻辑。
主要变更如下：
创建核心导航状态管理 (NavigationState.kt):
在 com.lyf.compose.core.nav3 包下创建了 NavigationState.kt。
实现了 NavigationState 类，用于管理多回退栈（TopLevelStack 和 SubStacks）。
实现了 rememberNavigationState Composable，用于初始化和保存导航状态。
实现了 MultiStackNavigator 类，实现了 Navigator 接口，处理多栈导航逻辑（包括 navigate, navigateRoot, pop）。
实现了 toEntries 扩展函数，将状态转换为 NavDisplay 可用的 NavEntry 列表，并自动应用 SaveableState 和 ViewModelStore 装饰器。
重构 AppNavHost.kt:
移除了原有的 rememberNavBackStack 和简单的 createNavigator 逻辑。
引入了 topLevelKeys 定义（Splash, Login, Home）。
使用 rememberNavigationState 初始化导航状态。
使用 MultiStackNavigator 作为导航控制器，并通过 LocalNavigator 提供给全局。
更新了 LaunchedEffect 中的登录态分流逻辑，使用 navigator.navigateRoot 切换根页面。
更新了 handleBack 逻辑，适配多栈结构，并保留了“双击返回退出”和“登录页返回退出”的逻辑。
更新了 NavDisplay 的使用方式，通过 navState.toEntries 提供页面入口。
MainActivity.kt:
保持不变，它只需调用 AppNavHost() 即可，内部实现已更新。
现在项目使用了更健壮的多栈导航架构，
符合 androidx.navigation3 的最佳实践，
并且能够更好地处理复杂的导航场景（如 Tab 切换、根页面切换等）。







我已经完成了重构，主要针对鉴权（Authentication）和重定向（Redirection）进行了增强。
重构内容总结
NavRegistry (路由注册表) 升级:
增加了 requiresLogin 参数，允许在注册路由时配置该页面是否需要登录。
新增 requiresLogin(key) 方法用于查询路由配置。
RouterRegistrations (路由配置):
更新了 registerAll 方法，将 HomeScreenRouter 标记为 requiresLogin = true。你可以根据需要标记其他页面。
NavigationState / MultiStackNavigator (导航控制器):
在 navigate 和 navigateRoot 方法中增加了拦截器。
如果目标页面需要登录且当前未登录，自动重定向到 LoginRouter。
AppNavHost (全局导航宿主):
重构了 HandleAuthNavigation 逻辑，使其更加智能和细粒度：
Splash 结束时：根据登录态分发到 Home 或 Login。
登出/Session失效时：如果当前停留在需要登录的页面，自动踢回 Login 页。
登录成功时：如果当前在 Login 页，自动跳转回 Home 页。
代码变更详情
1. NavRegistry.kt 增加了 requiresLogin 字段存储和查询能力。
2. RouterRegistrations.kt
   // 示例：将主页标记为需要登录
   NavRegistry.register(HomeScreenRouter::class, requiresLogin = true) { _ ->
   HomeScreen()
   }
3. NavigationState.kt (拦截器)
   override fun navigate(key: NavKey) {
   // 鉴权拦截：如果页面需要登录且未登录，重定向到登录页
   if (NavRegistry.requiresLogin(key) && !SessionManager.isLoggedIn()) {
   navigate(LoginRouter)
   return
   }
   // ... 原有逻辑
   }
4. AppNavHost.kt (状态监听)
   // 监听登录状态和当前页面的变化，处理自动跳转
   LaunchedEffect(isLoggedIn, splashFinished, navState.currentKey) {
   // ... Splash 逻辑 ...

   // 登出保护：如果在敏感页面掉线，强制回登录页
   if (!isLoggedIn && NavRegistry.requiresLogin(navState.currentKey)) {
   navigator.navigateRoot(LoginRouter)
   }

   // 登录自动跳转：登录成功后自动进主页
   if (isLoggedIn && navState.currentKey == LoginRouter) {
   navigator.navigateRoot(HomeScreenRouter)
   }
   }
   现在你的导航系统具备了基本的路由守卫（Route Guard）功能，不再是“裸奔”状态，且逻辑解耦，路由配置集中管理。
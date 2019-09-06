# reddit-fork-demo-app
Demo project show casing basic know-how of Android expertise. 

The project started as an interview challenge. The app aims to use most of the state of the art practices in Android development.
  - Kotlin
  - RxJava2 (RxKotlin)
  - DI with Koin
  - Retrofit + OkHttp + Moshi
  - Databinding
  - WorkManager
  - CLEAN architecture (without interactors)
  - Reddit API integration

Note: The application does not focus on design practices and esthetics. 

[SCOPE and REQUIREMENTS]

Create an Android app in Java/Kotlin, that consumes the Reddit API https://www.reddit.com/dev/api/
with two activities (master/detail): 
- first activity shows posts from the front page in a list. each post should have a Comments button.
- tapping a row item opens the link from the post (use intents to open using preferred browser)
- tapping the comments button opens the details activity2 which lists comments.

Post list should be paginated using Reddit's pagination mechanism.

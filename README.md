# Jetpack Compose - Movie App 🎬
<img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/681bcf7e-d0bb-4c16-b2a9-f521603bff0b" />

Movie App is developed with Jetpack Compose. In order to log in to the application, an account must first be created. Authentication is done with Firebase. 
The app can display movies that are currently playing and popular. On the See all movies page, all selected movies are shown with pagination. 
You can search for any movie, add it to the watchlist or look at the details of the movie. The movie details show the movie poster, a short description about the movie, director, rating, cast and trailers. 
On the profile screen you can change the app's language, theme and, if the device is Android 12 and above, you can also choose dynamic color. The profile picture is kept with Firebase storage. When the account is deleted, 
the watchlist and profile picture are also deleted from Firebase.

## Tech Stack 📚

* [Android Architecture Components](https://developer.android.com/topic/architecture)
    * [Navigation](https://developer.android.com/guide/navigation)
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
    * [Repository](https://developer.android.com/topic/architecture/data-layer?hl=en)
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
* [Room](https://developer.android.com/training/data-storage/room)
* [Retrofit](https://github.com/square/retrofit)
* [Coil](https://github.com/coil-kt/coil)
* [Firebase Auth](https://firebase.google.com/docs/auth)
* [Firebase Storage](https://firebase.google.com/docs/storage?hl=en)
* [Firebase Firestore](https://firebase.google.com/docs/firestore?hl=en)
* [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
* [Datastore](https://developer.android.com/topic/libraries/architecture/datastore)
* [Rating Bar](https://github.com/a914-gowtham/compose-ratingbar)
* [Youtube Player View](https://github.com/PierfrancescoSoffritti/android-youtube-player)
* [Turbine](https://developer.android.com/kotlin/flow/test#turbine)
* [Mockito](https://developer.android.com/training/testing/local-tests#mockable-library)


|                  | Light | Dark |
|------------------|-------|------|
| Login Screen     | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/158a2894-cd88-4207-a487-8c14ccf74d5f" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/dde7a4fe-088f-41d7-8e7d-71f51c9c1893" width="240" height="480"/>     |
| SignUp Screen  | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/6a8951f7-2983-4221-b094-3cacd4179e35" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/2c5c9b94-866d-4020-a56c-3d7dc2736ba7" width="240" height="480"/>     |
| Movies Screen      | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/c5c524c5-37cc-4b3b-9cdc-5b65b4d3b60a" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/c1302ea7-1a4a-489f-9ceb-f00ee26dd646)357dea" width="240" height="480"/>     |
| Search Screen    | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/e00edb29-8860-41ba-847e-38fafef7f6ca" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/17327c3f-a1b4-482d-8af0-a65da3263a57" width="240" height="480"/>     |
| WatchList Screen | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/65b51927-c45a-49c8-ae1a-84144fd7e727" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/bb1b8270-29fd-4db8-80d8-c237d67f093d" width="240" height="480"/>     |
| Profile Screen   | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/c42cca3d-cf8f-4db4-8764-5f2026968fc5" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/4f124572-0ee2-413a-8310-6a7239a6aee6" width="240" height="480"/>     |
| See All Screen   | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/fc0688dc-4798-405e-afa5-4c72d357de37" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/ba8a3925-c128-417b-b520-7b9c272937a8" width="240" height="480"/>     |
| Movie Details Screen      | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/35ab8f75-6be8-4de7-89a7-436eb641fafd" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/d7f02f9e-7dbb-4e01-af05-039d9295da6a" width="240" height="480"/> |
|  Movie Details Screen     |  <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/5cc2c76c-ee1e-4045-80c0-3d3fa2983e9a" width="240" height="480"/>       |  <img src="https://github.com/AhmetOcak/MovieApp/assets/73544434/bd7af4df-c02b-472a-afac-6632a4cebd99" width="240" height="480"/>          |


## Architecture 🏗
The app uses MVVM [Model-View-ViewModel] architecture to have a unidirectional flow of data, separation of concern, testability, and a lot more.

![mvvm](https://user-images.githubusercontent.com/73544434/197416569-d42a6bbe-126e-4776-9c8f-2791925f738c.png)

## API 📦
[TMDB Movie API](https://developer.themoviedb.org/reference/intro/getting-started)

## API KEY 🔑
* Generate a new api key from [here](https://www.themoviedb.org/settings/api).
* Open the page is gradle/local.properties. Define API key.
* ``` API_KEY="YOUR_API_KEY" ```


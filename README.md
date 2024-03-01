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
* [Splash API](https://developer.android.com/develop/ui/views/launch/splash-screen)

## Outputs 🖼

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
<!--
|                  |              |
|------------------|--------------|
| Video from app   | <video src="https://github.com/AhmetOcak/MovieApp/assets/73544434/cec4846c-026d-44ca-a456-9ee8b8751be7" width="240" height="480" /> |

-->

## Modularization 📦
![movie_app_modular_graph](https://github.com/AhmetOcak/MovieApp/assets/73544434/fd10aa83-f93e-46d3-a37e-ddc744627538)

<table>
  <tr>
   <td><strong>Name</strong>
   </td>
   <td><strong>Responsibilities</strong>
   </td>
   <td><strong>Key classes</strong>
   </td>
  </tr>
  <tr>
   <td><code>app</code>
   </td>
   <td>Brings everything together required for the app to function correctly. This module responsible for navigation.
   </td>
   <td><code>MovieApp, MainActivity</code><br>
   </td>
  </tr>
  <tr>
   <td><code>feature:1,</code><br>
   <code>feature:2</code><br>
   ...
   </td>
   <td>Functionality associated with a specific feature or user journey. Typically contains UI components and ViewModels which read data from other modules.<br>
   Examples include:<br>
   <ul>
      <li><a href="https://github.com/AhmetOcak/MovieApp/tree/master/feature/movie_details"><code>feature:movie_details</code></a> Movie details provide comprehensive information about a film, including its title, genre, release date, cast, and plot summary.</li>
      </ul>
   </td>
   <td><code>MovieDetailsScreen</code><br>
   <code>MovieDetailsViewModel</code>
   </td>
  </tr>
  <tr>
   <td><code>core:data</code>
   </td>
   <td>Fetching app data from multiple sources and sends it to the UI through the <code>core:domain</code> module.
   </td>
   <td><code>MovieRepository</code><br>
   </td>
  </tr>
  <tr>
   <td><code>core:designsystem</code>
   </td>
   <td>Design system which includes Core UI components (many of which are customized Material 3 components), app theme and icons.
   </td>
   <td>
   <code>MovieButton</code>    <code>MovieTextButton</code>    <code>MovieAppTheme</code> 
   </td>
  </tr>
  <tr>
   <td><code>core:ui</code>
   </td>
   <td>Composite UI components and resources used by feature modules. Unlike the <code>designsystem</code> module, it is dependent on the data layer since it renders models. 
   </td>
   <td> <code>MovieItem</code>
   </td>
  </tr>
  <tr>
   <td><code>core:common</code>
   </td>
   <td>Common classes shared between modules.
   </td>
   <td><code>UiText</code><br>
   <code>Response</code>
   </td>
  </tr>
  <tr>
   <td><code>core:network</code>
   </td>
   <td>Making network requests and handling responses from a remote data source.
   </td>
   <td><code>MovieApi</code>    <code>MovieRemoteDataSource</code>
   </td>
  </tr>
  <tr>
   <td><code>core:datastore</code>
   </td>
   <td>Storing persistent data using DataStore.
   </td>
   <td><code>MovieAppPreferenceDataSource</code><br>
   </td>
  </tr>
  <tr>
   <td><code>core:database</code>
   </td>
   <td>Local database storage using Room.
   </td>
   <td><code>WatchListDatabase</code><br>
   <code>Dao</code> classes
   </td>
  </tr>
   
  <tr>
   <td><code>core:model</code>
   </td>
   <td>Model classes used throughout the app.
   </td>
   <td><code>Movie</code><br>
   <code>MovieDetail</code><br>
   <code>WatchList</code>
   </td>
  </tr>
   
  <tr>
   <td><code>core:domain</code>
   </td>
   <td> It houses use cases. It serves as a bridge between the data layer's repositories and the UI.
   </td>  
     <td> <code>GetMovieTrailersUseCase</code>
   <code>GetMovieDetailsUseCase</code>
   </td>
  </tr>

   
  <tr>
   <td><code>core:navigation</code>
   </td>
   <td> Contains navigation routes.
   </td>   
      <td>    <code>MainDestinations</code>
   <code>HomeSections</code>
   </td>
  </tr>
</table>

## Architecture 🏗
The app uses MVVM [Model-View-ViewModel] architecture to have a unidirectional flow of data, separation of concern, testability, and a lot more.

![mvvm](https://user-images.githubusercontent.com/73544434/197416569-d42a6bbe-126e-4776-9c8f-2791925f738c.png)

## API 📦
[TMDB Movie API](https://developer.themoviedb.org/reference/intro/getting-started)

## Installation 🏗
* Generate a new api key from [here](https://www.themoviedb.org/settings/api).
* Open the local.properties. Define API key.
* ``` API_KEY="YOUR_API_KEY" ```
* Create a firebase project.
* Enable firebase auth, storage and firestore.
* Add google.services.json file to project.

## Upcoming Features
* Splash Screen
* Tv Series
* Actor Informations
# **Jani Project : Android Client**

`Jani Project Server GitHub`
https://github.com/NaJunYeop/Project_Jani_Spring_Boot_Websocket_Server

## **Use**.
- '**WebSocket** and **STOMP** Protocol

- **MVVM**(View - ViewModel - Model) **Pattern**

- **MVVM Pattern**을 적용하기 위해 사용한 **Libraries**
   - **Android AAC**(Android Architecture Components)
      
       - **ViewModel**
       - **LiveData**
       - **Data Binding** Library
      - **Room** Persistence Library

   - **RxJava**(Version 2.x)

   - **Retrofit**(Version 2.x)

<br><br>

## **Why WebSocket and STOMP Protocol ?**

---

**Instant Messaging App**을 구현하는 프로젝트를 계획하고 설계하면서 Server를 어떻게 구현할 것인지에 대한 많은 고민을 했습니다. 가장 중요한 것은 User들 간의 Message가 Server를 통해 **실시간**으로 전송되어야 한다는 점이었습니다. 따라서 **SpringBoot Framework**로 서버를 구현하며 `WebSocket Protocol`을 적용하여 **Bidirectional Connection**이 가능하게 구현하였습니다. 

이 때, **SpringBoo**t에서는 WebSocketMessageBrokerConfigurer Interface를 구현한 `@Configuration` Class를 생성하여 Message Broker(Spring 내부 In Memory Broker)를 설정해주면 Server Side Software Architecture로 **Publish/Subject Pattern**(이하 Pub/Sub Pattern)을 적용할 수 있습니다. 

하지만 `WebSocket Protocol`만으로는 **Pub/Sub Pattern**을 구현할 수 없는데 그 이유는 WebSocket Frame의 Message자체에는 Routing과 같은 정보가 없기 때문인데 이를 해결하기 위해 **Higher Layer**의 **Sub-Protocol**을 지정할 수 있습니다. 이 때, **Sub-Protocol**로 Protocol을 구현하기 쉽고(간단한 Frame구조를 가집니다.), 여러 **Language**, **Platform** 그리고 **Borker**와 **상호운용**이 가능한 **특성**을 가지는 `STOMP Protocol`을 선택하게 되었습니다. 

<br>

`STOMP Protocol`에 대한 공식문서의 설명은 다음과 같습니다.

>“**STOMP** provides an **interoperable wire format** so that STOMP clients can communicate with any STOMP message broker to provide **easy** and widespread messaging **interoperability** among many languages, platforms and brokers.”

>“**STOMP** is a very **simple** and **easy** to implement protocol, coming from the HTTP school of design; the server side may be hard to implement well, but it is very easy to write a client to get yourself connected.

<br><br>

## **Why MVVM Pattern ?**

---

`View ViewModel Model`로 **Module**화하여 서로 **의존성을 분리함**으로써 각 **Module**들을 유연하게 **유지/보수 및 관리**하는 것이 가능합니다.

 
`View`는 `ViewModel`로부터 받은 **Event**를 통해 User에게 보여 지는 `View`부분에 대한 Logic만을 처리합니다.

`Model`은 **Database** 혹은 **Network**로부터 받은 **Data**를 가공하여 `ViewModel`에게 전달합니다. 

`ViewModel`은 그 **Data**를 전달받아 **Business Logic**을 구현하고 그에 맞는 **Event**를 `View`에게 전달함으로써 각 **Module**간 동작의 의미가 명확해집니다.

<br><br>

## **MVVM Pattern**을 적용하기 위해 사용한 **Libraries**

---

- **Android AAC**(Android Architecture Components) 

   `Android AAC`는 **Test**와 **유지/보수**가 쉬운 **Application**을 디자인하도록 돕는 **Library 모음**입니다.

   - **ViewModel**
      
      `ViewModel`에 대한 Android 공식문서의 설명은 다음과 같습니다.
      >“The **ViewModel class** is designed to **store** and **manage** **UI-related data** in a **lifecycle conscious** way. The **ViewModel** class allows data to survive configuration changes such as screen rotations.”

      <br>
      
      즉, `View`의 **Lifecycle**을 고려하여 **Data**를 **저장**하고 **관리**하는 **Class**를 의미합니다. 따라서 `Android AAC`의 `ViewModel`을 사용하면 개발자가 직접 번거로운 `View`(Activity, Fragment)의 **Lifecycle**을 고려하지 않고 구현할 수 있습니다(**Memory Leak**의 위험성 감소). 일반적으로 `ViewModel`과 `View`는 **1 : N** 관계를 가집니다. 
   
   <br><br>

   - **LiveData**
      
      `LiveData`에 대한 Android 공식문서의 설명은 다음과 같습니다.
      >“**LiveDat**a is an **observable data holder class**. Unlike a regular observable, **LiveData** is **lifecycle-aware**, meaning it respects the lifecycle of other app components, such as activities, fragments, or services. This awareness ensures LiveData only updates app component observers that are in an **active lifecycle state**.”

      <br>

      즉, `ViewModel`에서 **Business Logic**을 만족하는 **Data**를 처리하고, 처리한 결과에 따른 **Event**(Observable Data)를 `View`의 **Lifecycle**을 고려하여 `View`가 **Active Lifecycle State**(`onStart(), onResume()`)일 경우에 **Update**(Notify/Publish)할 수 있습니다. 이 때 `View`는 `ViewModel`의 `LiveData`에 **Subscribe**하여 **Event Data**를 받아 원하는 `View`를 상황에 알맞게 Control할 수 있습니다.
   
   <br><br>

   - **Data Binding Library**

      `Data Binding Library`에 대한 Android 공식문서의 설명은 다음과 같습니다.
      >“The **Data Binding Library** is a **support library** that allows you to **bind UI components in your layouts** to **data sources** in your app using a **declarative format** rather than programmatically.”

      <br>

      즉, `View`의 **Layout**에 있는 **UI Component**들을 `View`에서 프로그래밍 방식으로 **Binding**하지 않고 **선언적 형식으로 Binding**하여 효율적인 **유지/보수**가 가능하게 합니다. 
      
      다시 말해, 기존 `Activity`혹은 `Fragment`에서 해왔던 `findViewById(), View의 Getter/Setter Method` 호출 등을 직접 하지 않고, **Layout**에 선언만 해주면 **내부적으로 Binding**이 됩니다. 또한 `View`의 값을 설정할때 역시 `ViewModel`의 `ObservableField` 객체를 통해 **One-Way Binding** 혹은 **Two-Way Binding**으로 선언하면 `ViewModel`이 자동으로 `View`의 값을 변경할 수 있습니다. 
   
   <br><br>

   - **Room Persistence Library**
      `Room Persistence Library`에 대한 Android 공식문서의 설명은 다음과 같습니다.
      >The Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.

      >The library helps you create a cache of your app's data on a device that's running your app. This cache, which serves as your app's single source of truth, allows users to view a consistent copy of key information within your app, regardless of whether users have an internet connection.

      <br>

      즉, **SQLite Database** 윗 단의 **Abstraction Layer**로써 **Object Relational Mapping**(ORM) **Library**입니다. 즉, **Table**을 **Object**로, **Object**를 **Table**에 Mapping해주는 **Library**입니다. 이는 기존 접근 방법과 비교해 **Boiler Plate Code**(상용구 코드)를 사용하지 않고 더욱 직관적이고 Clean하게 **Database**를 구현할 수 있습니다. **Database**에 접근할 때는 **Main Thread**(UI Thread)에서 직접 접근할 수 없는데 이 때 **Event-Based**인 `LiveData` 혹은 `Rxjava`를 적용하여 **Observable Sequence**를 활용하면 **AsyncTask**를 사용하는 것 보다 훨씬 간편하게 **Database**에 접근할 수 있습니다. 이 프로젝트에서는 `Rxjava 2.x Version`을 선택하였는데, **Database**에 대한 접근은 `View`의 **Lifecycle**과는 상관없이 **적시적소**에서 호출할 수 있어야 한다고 판단했기 때문에 `LiveData`보다는 `Rxjava`가 의미에 더욱 부합하다고 생각했습니다. 마지막으로 `Room` **Database**를 **Instantiate**하는 것은 굉장히 **Expensive**한 연산이기 때문에  **Singleton Pattern**으로 구현하였습니다.

<br><br>

- **Rxjava(Version 2.x)**

   `Rxjava`에 대한 공식문서의 설명은 다음과 같습니다.
   >“**RxJava** is a Java VM implementation of **Reactive** Extensions: a library for composing **asynchronous** and **event-based** programs by using **observable sequences**.


   >It extends the observer pattern to support sequences of data/events and adds operators that allow you to **compose sequences together** declaratively while abstracting away concerns about things like **low-level threading, synchronization, thread-safety** and **concurrent data structures**.”

   <br>

   즉, `Rxjava`는 **Observer Pattern**을 기반으로 한 **Reactive, Functional Programming Library**입니다. 특히 **Asynchronous**(비동기)하기 때문에 User의 요청에 따라 User가 체감하는 Business Logic을 처리하는 시간을 최소화할 수 있습니다. 
   
   또한 `.merge(), .zip(), .map()`등의 **비동기 연산**을 통해 **필터링, 조합, 변환**하여 Asynchronous Programming의 단점인 Callback Method의 늪에서 벗어날 수 있습니다. 
   
   그뿐만 아니라 **Event-Driven**이기 때문에 **Observable Sequence**에 변화가 생기거나 새로운 **Observer**가 **Subscribe**하게 되면 **Observable Sequence**(Stream)을 발행하여 변화를 공지합니다. 이 때, 간단하게 **Schedule Policy**를 설정해주면 **Database**나 **Network**와 접근하는 것과 같이 **Main Thread**(UI Thread)에서 할 수 없는 작업을 다른 **Thread**를 통해 손쉽게 할 수 있습니다.

<br><br>

- **Retrofit(Version 2.x)**

  ` Retrofit`에 대한 공식문서의 설명은 다음과 같습니다.
  >“A **type-safe** HTTP client for Java and Android”

  <br>

  즉, `Retrofit`은 **Type-Safe**하기 때문에 **Request**에 대한 **Response**를 **원하는 Object Type**으로 받을 수 있습니다. 이 때 `Retrofit`은 `Rxjava`를 공식지원하기 때문에 **Observable**형태로 결과 값을 받을 수 있습니다. 
  
  `HttpUrlConnection + AsyncTask`를 사용하는 것 보다 **Performance**가 좋을 뿐만 아니라 **직관적**이고(**가독성**이 매우 좋습니다.) 쉽게 구현할 수 있다는 장점이 **HttpClient**로 `Retrofit`을 선택하게 된 가장 큰 이유입니다. 이 `Retrofit` **Client Instance** 역시 빈번하게 사용되기 때문에 **Singleton Pattern**으로 구현하였습니다.



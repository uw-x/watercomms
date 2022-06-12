# Underwater Messaging Using Mobile Devices (SIGCOMM '22)

https://user-images.githubusercontent.com/174899/173208820-45f29503-06ed-47e7-bb95-b444bfa6cda5.mp4

# Smartphone code
The smartphone [code](smartphone/) is an Android Studio project of the real time demo application.

The project was originally built with Android Studio Arctic Fox 2020.3.1 Patch 2, and has been tested on Samsung Galaxy S9 phones.

Reproducing the demo video will require two Android smartphones, as well as [waterproof pouches](https://www.amazon.com/gp/product/B08S3SG5KF/ref=ppx_yo_dt_b_asin_title_o00_s00).

![uwater](https://user-images.githubusercontent.com/174899/173209246-7322f55d-ea3d-419e-abb4-1305da286c53.png)

We have documented areas of the code that correspond to key system components in our paper:

| System component      | Link to code |
| ----------- | ----------- |
| Protocol sequence logic      | [Code](smartphone/OceanRealDemo/app/src/main/java/com/example/root/ffttest2/SendChirpAsyncTask.java)       |
| Preamble generation (Alice)     | [Code](smartphone/OceanRealDemo/app/src/main/java/com/example/root/ffttest2/SendChirpAsyncTask.java)       |
| SNR estimation (Bob)     | [Code](smartphone/OceanRealDemo/app/src/main/java/com/example/root/ffttest2/SNR_freq.java#L4)       |
| Frequency band selection (Bob)      | [Code](smartphone/OceanRealDemo/app/src/main/java/com/example/root/ffttest2/Fre_adaptation.java#L13)       |
| Encoding feedback (Bob)    | [Code](smartphone/OceanRealDemo/app/src/main/java/com/example/root/ffttest2/SendChirpAsyncTask.java)       |
| Decoding feedback  (Alice)    | [Code](smartphone/OceanRealDemo/app/src/main/java/com/example/root/ffttest2/SendChirpAsyncTask.java)       |
| Encoding data packet (Alice)     | [Code](smartphone/OceanRealDemo/app/src/main/java/com/example/root/ffttest2/SendChirpAsyncTask.java)       |
| Decoding data packet (Bob)     | [Code](smartphone/OceanRealDemo/app/src/main/java/com/example/root/ffttest2/SendChirpAsyncTask.java)       |

![Screenshot from 2022-06-11 16-47-43](https://user-images.githubusercontent.com/174899/173208477-57eb4fb3-68ce-4651-afed-27ace099da47.png)

***Protocol sequence logic***

![Screenshot from 2022-06-11 16-47-59](https://user-images.githubusercontent.com/174899/173208480-980fb88b-820c-416f-9521-7d0b5f1eb524.png)

***Encoding and decoding data packets***

# MATLAB code
- [matlab/](matlab/) 
MATLAB code to process the collected data and reproduce the main result of the paper

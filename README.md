# Underwater Messaging Using Mobile Devices (SIGCOMM '22)

https://user-images.githubusercontent.com/174899/173207985-e6e0b2f8-1f8c-4463-acc9-5f3bef490728.mp4

# Smartphone code
The smartphone [code](smartphone/) is an Android Studio project of the real time demo application.

The project was originally built with Android Studio Arctic Fox 2020.3.1 Patch 2, and has been tested on Samsung Galaxy S9 phones.

Reproducing the demo video will require two Android smartphones, as well as [waterproof pouches](https://www.amazon.com/gp/product/B08S3SG5KF/ref=ppx_yo_dt_b_asin_title_o00_s00).

We have documented key areas of the code that correspond to the system design in Section 2 of the paper.

<ul>
![Screenshot from 2022-06-11 16-47-43](https://user-images.githubusercontent.com/174899/173208477-57eb4fb3-68ce-4651-afed-27ace099da47.png)

![Screenshot from 2022-06-11 16-47-59](https://user-images.githubusercontent.com/174899/173208480-980fb88b-820c-416f-9521-7d0b5f1eb524.png)

  <li>The logic for controlling the sequence of steps in our protocol is in SendChirpAsycTask:148</li>
  <li>Preamble generation (Alice): </li>
  <li>SNR estimation (Bob): xxx </li>
  <li>Frequency band selection (Bob): xxx</li>
  <li>Encoding feedback (Bob): xxx </li>
  <li>Decoding feedback (Alice): xxx </li>
  <li>Encoding data packet (Alice): xxx </li>
  <li>Decoding data packet (Bob): xxx </li>
</ul>


# MATLAB code
- [matlab/](matlab/) 
MATLAB code to process the collected data and reproduce the main result of the paper

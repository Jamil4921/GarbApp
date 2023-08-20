FROM openjdk:8-jdk

ENV ANDROID_SDK_ROOT /opt/android-sdk

RUN mkdir -p $ANDROID_SDK_ROOT
RUN wget https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip -O /tmp/sdk-tools.zip && \
    unzip /tmp/sdk-tools.zip -d $ANDROID_SDK_ROOT && \
    rm /tmp/sdk-tools.zip

ENV PATH $PATH:$ANDROID_SDK_ROOT/tools/bin

RUN yes | sdkmanager --licenses && \
    sdkmanager "platform-tools" "build-tools;29.0.2" "platforms;android-29"

WORKDIR /app

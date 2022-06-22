LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := fftw3
LOCAL_SRC_FILES := $(LOCAL_PATH)/fftw3/lib/$(TARGET_ARCH_ABI)/libfftw3.a
LOCAL_EXPORT_C_INCLUDES := ./fftw3/include
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := native-lib
LOCAL_SRC_FILES := $(LOCAL_PATH)/src/main/cpp/native-lib.cpp
LOCAL_LDLIBS := -llog -lm
LOCAL_STATIC_LIBRARIES := fftw3
include $(BUILD_SHARED_LIBRARY)

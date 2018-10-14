LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := photofilterssdk
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	/Users/antonhorenko/GIT/AndroidPhotoFilters-master/photofilterssdk/Android.mk \
	/Users/antonhorenko/GIT/AndroidPhotoFilters-master/photofilterssdk/Application.mk \
	/Users/antonhorenko/GIT/AndroidPhotoFilters-master/photofilterssdk/src/main/jni/NativeImageProcessor.cpp \

LOCAL_C_INCLUDES += /Users/antonhorenko/GIT/AndroidPhotoFilters-master/photofilterssdk/src/main/jni
LOCAL_C_INCLUDES += /Users/antonhorenko/GIT/AndroidPhotoFilters-master/photofilterssdk/src/debug/jni

include $(BUILD_SHARED_LIBRARY)

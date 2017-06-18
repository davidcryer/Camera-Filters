#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_davidcryer_camerafilters_screens_filter_FilterUiWrapper_helloWorld(JNIEnv* env, jobject thiz) {
    return env->NewStringUTF("Hello, World!\n");
}
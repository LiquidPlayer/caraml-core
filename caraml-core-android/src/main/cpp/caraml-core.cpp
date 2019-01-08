#include <jni.h>
#include <node.h>
#include "v8.h"

using namespace v8;

void Init(Local<Object> target)
{
}

NODE_MODULE_CONTEXT_AWARE(caramlcore,Init)

extern "C" void JNICALL Java_org_liquidplayer_addon_Caramlcore_register(JNIEnv* env, jobject thiz)
{
    _register_caramlcore();
}

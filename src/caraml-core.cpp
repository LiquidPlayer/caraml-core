#include <node.h>
#include "v8.h"
#include "caraml-core.h"

using namespace v8;

void Init(Local<Object> target)
{
}

NODE_MODULE_CONTEXT_AWARE(caramlcore,Init)

void register_caramlcore()
{
    _register_caramlcore();
}

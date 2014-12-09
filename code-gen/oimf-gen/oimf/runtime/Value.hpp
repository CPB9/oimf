// File is generated automatically. DO NOT EDIT
#include<oimf/U8ListIsomorphicWith.hpp>
#include<oimf/U32IsomorphicWith.hpp>
#include<oimf/I64IsomorphicWith.hpp>
#include<oimf/AnsiStringValueMapIsomorphicWith.hpp>
#include<oimf/F64IsomorphicWith.hpp>
#include<oimf/TraitApplication.hpp>
#include<oimf/F32IsomorphicWith.hpp>
#include<oimf/U64IsomorphicWith.hpp>
#include<oimf/I32IsomorphicWith.hpp>

namespace oimf::runtime {

class Value : AnsiStringValueMapIsomorphicWith, I32IsomorphicWith, U32IsomorphicWith, I64IsomorphicWith, U64IsomorphicWith, F32IsomorphicWith, F64IsomorphicWith, U8ListIsomorphicWith {
public:
	TraitApplication traitApplication();
	bool isMap();
	bool isI32();
	bool isU32();
	bool isI64();
	bool isU64();
	bool isF32();
	bool isF64();
	bool isList();
};

}

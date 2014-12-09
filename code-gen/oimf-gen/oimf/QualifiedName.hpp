// File is generated automatically. DO NOT EDIT
#include<oimf/AnsiStringList.hpp>
#include<oimf/AnsiString.hpp>
#include<oimf/AnsiStringIsomorphicWith.hpp>
#include<oimf/Bounded.hpp>

namespace oimf {

class QualifiedName : AnsiStringIsomorphicWith, Bounded {
public:
	AnsiStringList parts();
	AnsiString last();
	void dropLast();
};

}

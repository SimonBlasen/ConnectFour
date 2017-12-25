TEMPLATE = app
CONFIG += console c++11
CONFIG -= app_bundle
CONFIG -= qt

SOURCES += main.cpp
#LIBS += -lfann

win32:CONFIG(release, debug|release): LIBS += -L$$PWD/../../../../FANN-2.2.0-Source/bin/ -lfannfloat
else:win32:CONFIG(debug, debug|release): LIBS += -L$$PWD/../../../../FANN-2.2.0-Source/bin/ -lfannfloatd
else:unix: LIBS += -L$$PWD/../../../../FANN-2.2.0-Source/bin/ -lfannfloat

INCLUDEPATH += $$PWD/../../../../FANN-2.2.0-Source/bin
DEPENDPATH += $$PWD/../../../../FANN-2.2.0-Source/bin

win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../../../../FANN-2.2.0-Source/bin/libfannfloat.a
else:win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../../../../FANN-2.2.0-Source/bin/libfannfloatd.a
else:win32:!win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../../../../FANN-2.2.0-Source/bin/fannfloat.lib
else:win32:!win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../../../../FANN-2.2.0-Source/bin/fannfloatd.lib
else:unix: PRE_TARGETDEPS += $$PWD/../../../../FANN-2.2.0-Source/bin/libfannfloat.a

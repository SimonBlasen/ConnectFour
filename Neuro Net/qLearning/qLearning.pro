TEMPLATE = app
CONFIG += console c++11
CONFIG -= app_bundle
CONFIG -= qt

SOURCES += main.cpp \
    memoryitem.cpp \
    game.cpp \
    qplayer.cpp \
    netutils.cpp \
    gameanalyzer.cpp
LIBS += -lfann

HEADERS += \
    memoryitem.h \
    game.h \
    qplayer.h \
    netutils.h \
    gameanalyzer.h

#include <windows.h>
#include <fstream>
#include <string>
#include <future>
#include <filesystem>
#include <csignal>
#include <cstdlib>
#include <cstdio>

using namespace std;

class NoSleeper {
public:
    NoSleeper() = default;

    promise<void> p;
    future<void> fut;

    void run() {
        fut = p.get_future();
        auto flagsToRun = flags | (display ? ES_DISPLAY_REQUIRED : 0);
        SetThreadExecutionState(flagsToRun);
        running = true;
    }

    void await() {
        fut.wait();
    }

    void stop() {
        if (running) {
            running = false;
            p.set_value();
        }
    }

    ~NoSleeper() {
        stop();
        SetThreadExecutionState(ES_CONTINUOUS);
    }

    bool display = false;
private:
    bool running = false;
    DWORD flags = ES_CONTINUOUS | ES_SYSTEM_REQUIRED; // | ES_AWAYMODE_REQUIRED;
};


template<typename T, typename R>
void doWithFinally(T func, R finnaly) {
    __try{
            func();
    }
    __finally{
            finnaly();
    }
}

int main(int argc, char *argv[]) {
    auto name = string("no-sleeper");

    auto mutexName = "Local\\" + name;

    CreateMutexA(nullptr, FALSE, mutexName.c_str());
    if (GetLastError() == ERROR_ALREADY_EXISTS) {
        return -1;
    }

    auto noSleeper = NoSleeper();
    noSleeper.display = argc > 1 && argv[1] == string("display");

    auto fileName = "kill-" + name + ".bat";
    ofstream killFile(fileName, ios::out);

    killFile << "taskkill /IM " << name << ".exe" << " /F" << '\n';
    killFile << "DEL " << fileName << '\n';
    killFile.close();

    auto terminationHandler = [](int code) {
        try {
            remove("kill-no-sleeper.bat");
        } catch (...) {
            exit(-123);
        }
        exit(0);
    };
    signal(SIGINT, terminationHandler);
    signal(SIGTERM , terminationHandler);


    noSleeper.run();
    noSleeper.await();
    return 0;
}

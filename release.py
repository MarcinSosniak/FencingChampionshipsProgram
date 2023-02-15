import sys
import subprocess
import shutil

RELEASE_FOLDER_BASE_FILE_NAME = 'ProgramZawody_'
JAR_FILNAME_BASE = "Szermierka-"


def main():
    version = get_version()
    gradle_set_version(version)
    subprocess.run('gradlew jar', shell=True)
    copy_release_folder(version)
    copy_jar(version)
    update_run_script(version)
    run_git(version)


def update_run_script(version):
    old_run = None
    with open(get_release_folder_fpath(version) + "\\run.bat", "r") as f:
        old_run = f.readline()
    with open(get_release_folder_fpath(version) + "\\run.bat", "w") as f:
        f.write(old_run.split(JAR_FILNAME_BASE)[0] + get_jar_filename(version) + '\n')


def run_git(version):
    if "--skip-git" in sys.argv:
        return
    subprocess.run('git reset HEAD', shell=True)
    subprocess.run('git add build.gradle', shell=True)
    subprocess.run(f'git commit -m \"RELEASE {version}\"')
    subprocess.run(f'git tag  v{version}')


def get_release_folder_fpath(version):
    return "releases\\" + get_release_folder_name(version)


def get_release_folder_name(version):
    return RELEASE_FOLDER_BASE_FILE_NAME + version.replace('.', '_')


def copy_release_folder(version):
    shutil.copytree("zawody_base_folder", get_release_folder_fpath(version))


def copy_jar(version):
    shutil.copy("build\\libs\\" + get_jar_filename(version),
                get_release_folder_fpath(version) + "\\" + get_jar_filename(version))


def get_jar_filename(version):
    return JAR_FILNAME_BASE + version + ".jar"


def get_version():
    if (len(sys.argv) >= 2):
        return sys.argv[1]
    else:
        print("version number is required")
        sys.exit(1)


def gradle_set_version(version):
    new_lines = []
    with open("build.gradle", "r") as gradleF:
        for line in gradleF:
            if line.startswith("version "):
                new_lines.append(f"version '{version}'\n")
            else:
                new_lines.append(line)
    with open("build.gradle", "w") as gradleF:
        gradleF.write(''.join(new_lines))


if __name__ == "__main__":
    main()

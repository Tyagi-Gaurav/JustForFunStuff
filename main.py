import subprocess


class ExecutionError(Exception):
    pass


def run_shell_command(cmd, **kwargs):
    directory = kwargs.pop('directory', ".")
    p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, cwd=directory)
    retval = p.wait()
    ignore_errors = kwargs.pop('ignore_errors', False)
    if not ignore_errors and retval != 0:
        raise ExecutionError()
    return (p.stdout.readlines(), retval)


def commit_counts_since_last_tag():
    (lines, _) = run_shell_command("git log $(git describe --tags --abbrev=0)..HEAD --oneline | wc -l")
    return int(lines[0].decode("utf-8").strip())


def get_new_tag():
    (lines, _) = run_shell_command(
        "git describe --tags --abbrev=0 | awk -F. -v OFS=. 'NF==1{print ++$NF}; NF>1{if(length($NF+1)>length($NF))$(NF-1)++; $NF=sprintf(\"%0*d\", length($NF), ($NF+1)%(10^length($NF))); print}'")
    return lines[0].decode("utf-8").strip()


def create_and_push_new_tag(tag):
    run_shell_command(f"git tag -a {tag} -m \"Next Version\"")
    run_shell_command("git push")


def delete_old_image():
    run_shell_command("docker rmi chonku/jffs-ui:LATEST")


def create_new_image_with(tag):
    run_shell_command(f"docker build -t chonku/jffs-ui:LATEST -t chonku/jffs-ui:{tag} .", directory="./jffs-ui")


def get_current_tag():
    (lines, _) = run_shell_command("git describe --tags --abbrev=0")
    return lines[0].decode("utf-8").strip()


def push_image_with_all_tags():
    run_shell_command("docker push --all-tags chonku/jffs-ui")

count = commit_counts_since_last_tag()
if count > 0:
    print("Changes found. Creating a new tag.")
    new_tag = get_new_tag()
    print(f"New tag would be {new_tag}")
    create_and_push_new_tag(new_tag)
    delete_old_image()
    create_new_image_with(new_tag)
else:
    new_tag = get_current_tag()
    print(f"Using last tag {new_tag} to create image")
    #TODO If image already exists then don't create
    # create_new_image_with(new_tag)

push_image_with_all_tags()



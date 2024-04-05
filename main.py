import subprocess
import docker

client = docker.from_env()


class ExecutionError(Exception):
    pass


def run_shell_command(cmd, ignore_errors=False):
    p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    retval = p.wait()
    if retval != 0:
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
    run_shell_command(f"git tag -a {tag}")
    run_shell_command("git push")


count = commit_counts_since_last_tag()
if count > 0:
    print("Create a new image")
    new_tag = get_new_tag()
    create_and_push_new_tag(new_tag)
else:
    print("Use last image")

resp = client.images.list()
for image in resp:
    all_tags = image.attrs["RepoTags"]
    for tag in all_tags:
        if "chonku/jffs-ui" in tag:
            client.images.remove(tag)

client.images.build()
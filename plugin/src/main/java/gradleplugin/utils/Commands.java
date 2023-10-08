package gradleplugin.utils;

public class Commands {
    public static final String COMMAND_GET_LAST_TAG = "git tag -l --sort=version:refname";
    public static final String COMMAND_GET_BRANCH = "git rev-parse --abbrev-ref HEAD";
    public static final String COMMAND_CREATE_TAG = "git tag ";
    public static final String COMMAND_PUSH_TAG = "git push --atomic origin ";
    public static final String COMMAND_IS_GIT_DIRECT = "git rev-parse --is-inside-work-tree";
    public static final String COMMAND_DIFF = "git diff";
    public static final String COMMAND_DIFF_CACHE = "git diff --cached";
    public static final String COMMAND_UNCOMM_TAG = "git describe --all --long";
    public static final String COMMAND_GET_CURRENT_TAG = "git reflog --decorate -1";



}

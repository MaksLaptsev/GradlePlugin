package gradleplugin.task;

import gradleplugin.exeptions.GitNotFoundException;
import gradleplugin.exeptions.TaggedException;
import gradleplugin.exeptions.UncommittedException;
import gradleplugin.utils.Commands;
import gradleplugin.utils.StringBuilderFromStream;
import gradleplugin.utils.TagUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;

public class CreateTag extends DefaultTask{
    private Process process;

    public CreateTag() {
        this.setGroup("Custom gradle plugin homework");
    }

    @TaskAction
    public void checkAndCreateTagIfNeeded() {
        boolean isGitDir;
        boolean isUncommitted;
        boolean isUncommitted_cache;
        boolean isTagExist;
        try{
            isGitDir = Boolean.parseBoolean(out(Commands.COMMAND_IS_GIT_DIRECT)
                    .replace("\n","").split(" ")[0]);
            if (!isGitDir){
                System.out.println("Is not a Git directory!!");
                throw new GitNotFoundException("Is not a Git directory!!");
            }

            isUncommitted = !(out(Commands.COMMAND_DIFF) == null || out(Commands.COMMAND_DIFF).equals(""));
            isUncommitted_cache = !(out(Commands.COMMAND_DIFF_CACHE) == null || out(Commands.COMMAND_DIFF_CACHE).equals(""));
            if (isUncommitted || isUncommitted_cache){
                String uncommittedTag = TagUtils.createUncommittedTag(out(Commands.COMMAND_UNCOMM_TAG));
                System.out.println("There are uncommitted changes");
                System.out.println(uncommittedTag);
                throw new UncommittedException("There are uncommitted changes");
            }

            double currentTag = getCurrentTag(Commands.COMMAND_GET_CURRENT_TAG);
            isTagExist = currentTag != 0.0;

            if (isTagExist){
                System.out.println("The tag already exists on the current commit");
                System.out.println(currentTag);
                throw new TaggedException("The tag already exists on the current commit!!");
            }

            process = Runtime.getRuntime().exec(Commands.COMMAND_GET_LAST_TAG);
            String lastTag = TagUtils.getLastTagStr(process);
            String branch = out(Commands.COMMAND_GET_BRANCH);
            String newTag = TagUtils.createTag(lastTag,branch);

            process = Runtime.getRuntime().exec(Commands.COMMAND_CREATE_TAG+newTag);
            System.out.println("new created tag: "+newTag);

            process = Runtime.getRuntime().exec(Commands.COMMAND_PUSH_TAG+branch+" "+newTag);

        }catch (GitNotFoundException | UncommittedException | TaggedException | IOException e){
            System.out.println(e.getMessage());
        }

    }

    private String out(String command){
        String out;
        try{
            process = Runtime.getRuntime().exec(command);
            out = StringBuilderFromStream.getOutResult(process);
        }catch (IOException e){
            out = "";
            System.out.println(e.getMessage());
        }
        return out;
    }

    private double getCurrentTag(String command){
        double tag = 0.0;
        String[] out = out(command)
                .replace("\n","")
                .replace(":","")
                .split(" ");
        for (int i = 0; i < out.length; i++){
            if (out[i].equals("tag")){
                tag = TagUtils.getNumericTag(out[i+1]);
            }
        }
        return tag;
    }
}

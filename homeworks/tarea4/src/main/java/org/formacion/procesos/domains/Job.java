package org.formacion.procesos.domains;

import java.time.Instant;

/**
 * @author eduglezexp
 * @version 1.0
 */

public class Job {
    public JobId id;
    public JobType type;
    public String commandLine;
    public long pid;
    public int exitCode;
    public JobStatus status;
    public Instant startedAt;
    public Instant finishedAt;
    public long durationMs;
    public String logPath;
    public String stdoutPath;
    public String stderrPath;
    public String errorMessage;

    public Job() {
        this.id = new JobId();
        this.type = null;
        this.commandLine = null;
        this.pid = 0;
        this.exitCode = 0;
        this.status = null;
        this.startedAt = null;
        this.finishedAt = null;
        this.durationMs = 0;
        this.logPath = null;
        this.stdoutPath = null;
        this.stderrPath = null;
        this.errorMessage = null;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", type=" + type +
                ", commandLine='" + commandLine + '\'' +
                ", pid=" + pid +
                ", exitCode=" + exitCode +
                ", status=" + status +
                ", startedAt=" + startedAt +
                ", finishedAt=" + finishedAt +
                ", durationMs=" + durationMs +
                ", logPath='" + logPath + '\'' +
                ", stdoutPath='" + stdoutPath + '\'' +
                ", stderrPath='" + stderrPath + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}

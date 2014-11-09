package com.dslplatform.patterns;

/**
 * Report object should be used for reducing round-trips from server.
 * When request from server should return multiple data sources
 * report concept with it's arguments and results is appropriate.
 * <p>
 * DSL example:
 * <blockquote><pre>
 * module Web {
 *   report GatherStartupPage {
 *     int perPage;
 *     int userID;
 *     date after;
 *     List&lt;Question&gt; popularQuestions 'it => it.date > after' limit perPage;
 *     UserInfo userInfo 'it => it.id == userID';
 *     List&lt;Menu&gt; menu 'it => it.active' order by index;
 *     count&lt;Question&gt; totalQuestions;
 *   }
 *   aggregate Question {
 *     date date;
 *     string text;
 *   }
 *   aggregate UserInfo(id) {
 *     int id;
 *     string name;
 *   }
 *   SQL Menu 'SELECT name, active FROM StartupMenu' {
 *       String name;
 *       bool active;
 *   }
 * }
 * </pre></blockquote>
 */
public interface Report<T> {}

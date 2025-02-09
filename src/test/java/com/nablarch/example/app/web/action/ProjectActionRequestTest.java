package com.nablarch.example.app.web.action;

import com.nablarch.example.app.entity.Project;
import com.nablarch.example.app.test.ExampleHttpRequestTest;
import com.nablarch.example.app.test.ExampleTestCaseInfo;
import com.nablarch.example.app.test.advice.SignedInAdvice;
import com.nablarch.example.app.test.ExampleHttpRequestTestSupport;
import nablarch.common.web.session.SessionUtil;
import nablarch.core.util.DateUtil;
import nablarch.fw.ExecutionContext;
import nablarch.test.Assertion;
import nablarch.test.core.file.FileSupport;
import org.junit.jupiter.api.Test;

/**
 * {@link ProjectAction}のリクエスト単体テストクラス。
 *
 * @author Nabu Rakutaro
 */
@ExampleHttpRequestTest(baseUri = "/action/project/")
class ProjectActionRequestTest {
    ExampleHttpRequestTestSupport support;

    /**
     * プロジェクト登録画面表示正常系ケース。
     */
    @Test
    void newEntityNormal() {
        support.execute("newEntityNormal", new SignedInAdvice());
    }

    /**
     * プロジェクト登録確認画面表示正常系ケース。
     */
    @Test
    void confirmOfCreateNormal() {
        support.execute("confirmOfCreateNormal", new SignedInAdvice() {

            @Override
            public void afterExecute(ExampleTestCaseInfo testCaseInfo,
                                     ExecutionContext context) {
                support.assertEntity(testCaseInfo.getSheetName(),
                        "project" + testCaseInfo.getTestCaseNo(),
                        context.getRequestScopedVar("project"));
            }
        });
    }

    /**
     * プロジェクト登録確認異常系ケース。
     */
    @Test
    void confirmOfCreateAbNormal() {
        support.execute("confirmOfCreateAbNormal", new SignedInAdvice());
    }

    /**
     * プロジェクト登録正常系ケース。
     */
    @Test
    void createNormal() {
        support.execute("createNormal", new SignedInAdvice() {
            @Override
            public void signedInBeforeExecute(ExampleTestCaseInfo testCaseInfo,
                                              ExecutionContext context) {
                SessionUtil.delete(context, "project");
                SessionUtil.put(context, "project", createMinimalProject());
            }
        });
    }

    /**
     * プロジェクト登録異常系ケース。
     */
    @Test
    void createAbNormal() {
        support.execute("createAbNormal", new SignedInAdvice() {
            @Override
            public void signedInBeforeExecute(ExampleTestCaseInfo testCaseInfo,
                                              ExecutionContext context) {
                SessionUtil.delete(context, "project");
                SessionUtil.put(context, "project", createMinimalProject());
            }
        });
    }

    /**
     * プロジェクト登録画面へ戻る正常系ケース。
     */
    @Test
    void backToNewNormal() {
        support.execute("backToNewNormal", new SignedInAdvice() {
            @Override
            public void signedInBeforeExecute(ExampleTestCaseInfo testCaseInfo,
                                              ExecutionContext context) {
                SessionUtil.delete(context, "project");
                Project project = createMinimalProject();
                project.setProjectStartDate(DateUtil.getDate("20180101"));
                SessionUtil.put(context, "project", project);
            }

            @Override
            public void afterExecute(ExampleTestCaseInfo testCaseInfo,
                                     ExecutionContext context) {
                support.assertEntity(testCaseInfo.getSheetName(),
                        "project" + testCaseInfo.getTestCaseNo(),
                        context.getRequestScopedVar("project"));
            }
        });
    }

    /**
     * 登録完了画面表示正常系ケース。
     */
    @Test
    void completeOfCreateNormal() {
        support.execute("completeOfCreateNormal", new SignedInAdvice());
    }

    /**
     * プロジェクト検索画面初期表示正常系ケース。
     */
    @Test
    void indexNormal() {
        support.execute("indexNormal", new SignedInAdvice() {

            @Override
            public void afterExecute(ExampleTestCaseInfo testCaseInfo,
                                     ExecutionContext context) {
                // 検索フォームの確認
                support.assertEntity(testCaseInfo.getSheetName(),
                        "searchForm" + testCaseInfo.getTestCaseNo(),
                        context.getRequestScopedVar("searchForm"));

                // 検索結果の確認
                support.assertBeanList(testCaseInfo.getSheetName(), "project", "searchResult", testCaseInfo, context);
            }
        });
    }

    /**
     * プロジェクト検索正常系ケース。
     */
    @Test
    void listNormal() {
        support.execute("listNormal", new SignedInAdvice() {

            @Override
            public void afterExecute(ExampleTestCaseInfo testCaseInfo,
                                     ExecutionContext context) {
                String sheetName = testCaseInfo.getSheetName();

                // 検索フォームの確認
                support.assertEntity(sheetName, "searchForm" + testCaseInfo.getTestCaseNo(),
                        context.getRequestScopedVar("searchForm"));

                // 検索結果の確認
                support.assertBeanList(sheetName, "project", "searchResult", testCaseInfo, context);
            }
        });
    }

    /**
     * プロジェクト検索異常系ケース。
     */
    @Test
    void listAbNormal() {
        support.execute("listAbNormal", new SignedInAdvice());
    }

    /**
     * プロジェクト詳細表示正常系ケース。
     */
    @Test
    void showNormal() {
        support.execute("showNormal", new SignedInAdvice() {

            @Override
            public void afterExecute(ExampleTestCaseInfo testCaseInfo,
                                     ExecutionContext context) {
                support.assertEntity(testCaseInfo.getSheetName(),
                        "projectDto" + testCaseInfo.getTestCaseNo(),
                        context.getRequestScopedVar("form"));
            }
        });
    }

    /**
     * プロジェクト詳細表示異常系ケース。
     */
    @Test
    void showAbNormal() {
        support.execute("showAbNormal", new SignedInAdvice());
    }

    /**
     * 更新画面表示正常系ケース。
     */
    @Test
    void editNormal() {
        support.execute("editNormal", new SignedInAdvice() {

            @Override
            public void signedInBeforeExecute(ExampleTestCaseInfo testCaseInfo,
                                              ExecutionContext context) {
                SessionUtil.delete(context, "project");
                SessionUtil.put(context, "project", createMinimalProject());
            }

            @Override
            public void afterExecute(ExampleTestCaseInfo testCaseInfo,
                                     ExecutionContext context) {
                support.assertEntity(testCaseInfo.getSheetName(),
                        "form" + testCaseInfo.getTestCaseNo(),
                        context.getRequestScopedVar("form"));

                if (SessionUtil.get(context, "project") == null) {
                    Assertion.fail("No Project in session.");
                }
            }
        });
    }

    /**
     * 更新画面表示異常系ケース。
     */
    @Test
    void editAbNormal() {
        support.execute("editAbNormal", new SignedInAdvice());
    }

    /**
     * プロジェクト更新確認画面表示正常系ケース。
     */
    @Test
    void confirmOfUpdateNormal() {
        support.execute("confirmOfUpdateNormal", new SignedInAdvice() {

            @Override
            public void signedInBeforeExecute(ExampleTestCaseInfo testCaseInfo,
                                              ExecutionContext context) {
                SessionUtil.delete(context, "project");
                SessionUtil.put(context, "project", createMinimalProject());
            }

            @Override
            public void afterExecute(ExampleTestCaseInfo testCaseInfo,
                                     ExecutionContext context) {
                support.assertEntity(testCaseInfo.getSheetName(),
                        "project" + testCaseInfo.getTestCaseNo(),
                        context.getRequestScopedVar("project"));
            }
        });
    }

    /**
     * プロジェクト更新確認画面表示異常系ケース。
     */
    @Test
    void confirmOfUpdateAbNormal() {
        support.execute("confirmOfUpdateAbNormal", new SignedInAdvice());
    }

    /**
     * プロジェクト更新画面へ戻る正常系ケース。
     */
    @Test
    void backToEditNormal() {
        support.execute("backToEditNormal", new SignedInAdvice() {
            @Override
            public void signedInBeforeExecute(ExampleTestCaseInfo testCaseInfo,
                                              ExecutionContext context) {
                SessionUtil.delete(context, "project");
                Project project = createMinimalProject();
                project.setProjectStartDate(DateUtil.getDate("20180101"));
                SessionUtil.put(context, "project", project);
            }

            @Override
            public void afterExecute(ExampleTestCaseInfo testCaseInfo,
                                     ExecutionContext context) {
                support.assertEntity(testCaseInfo.getSheetName(),
                        "project" + testCaseInfo.getTestCaseNo(),
                        context.getRequestScopedVar("project"));
            }
        });
    }

    /**
     * プロジェクト更新正常系ケース。
     */
    @Test
    void updateNormal() {
        support.execute("updateNormal", new SignedInAdvice() {
            @Override
            public void signedInBeforeExecute(ExampleTestCaseInfo testCaseInfo,
                                              ExecutionContext context) {
                SessionUtil.delete(context, "project");
                Project project = createMinimalProject();
                project.setProjectId(99998);
                project.setVersion(0L);
                SessionUtil.put(context, "project", project);
            }
        });
    }

    /**
     * プロジェクト更新異常系ケース。
     */
    @Test
    void updateAbNormal() {
        support.execute("updateAbNormal", new SignedInAdvice() {
            @Override
            public void signedInBeforeExecute(ExampleTestCaseInfo testCaseInfo,
                                              ExecutionContext context) {
                SessionUtil.delete(context, "project");
                Project project = createMinimalProject();
                project.setProjectId(99998);
                project.setVersion(0L);
                SessionUtil.put(context, "project", project);
            }
        });
    }

    /**
     * プロジェクト更新完了画面表示正常系ケース。
     */
    @Test
    void completeOfUpdateNormal() {
        support.execute("completeOfUpdateNormal", new SignedInAdvice());
    }

    /**
     * プロジェクト削除正常系ケース。
     */
    @Test
    void deleteNormal() {
        support.execute("deleteNormal", new SignedInAdvice() {
            @Override
            public void signedInBeforeExecute(ExampleTestCaseInfo testCaseInfo,
                                              ExecutionContext context) {
                SessionUtil.delete(context, "project");
                Project project = createMinimalProject();
                project.setProjectId(99999);
                SessionUtil.put(context, "project", project);
            }
        });
    }

    /**
     * プロジェクト削除異常系ケース。
     */
    @Test
    void deleteAbNormal() {
        support.execute("deleteAbNormal", new SignedInAdvice());
    }

    /**
     * プロジェクト削除完了画面表示正常系ケース。
     */
    @Test
    void completeOfDeleteNormal() {
        support.execute("completeOfDeleteNormal", new SignedInAdvice());
    }

    /**
     * プロジェクト一覧ダウンロード正常系ケース。
     */
    @Test
    void downloadNormal() {
        support.execute("downloadNormal", new SignedInAdvice() {

            @Override
            public void afterExecute(ExampleTestCaseInfo testCaseInfo, ExecutionContext context) {
                FileSupport fileSupport = new FileSupport(ProjectActionRequestTest.class);
                fileSupport.assertFile("CSVの検証処理でエラーが発生しました。", "downloadNormal");
            }
        });
    }

    /**
     * * プロジェクト一覧ダウンロード異常系ケース。
     */
    @Test
    void downloadAbNormal() {
        support.execute("downloadAbNormal", new SignedInAdvice());
    }

    /**
     * 最小限の項目を設定したプロジェクトエンティティを返す。
     *
     * @return プロジェクトエンティティ
     */
    private Project createMinimalProject() {
        Project project = new Project();
        project.setProjectName("プロジェクト００１");
        project.setProjectClass("s");
        project.setProjectType("development");
        project.setClientId(1);
        return project;
    }
}
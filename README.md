# DBtoDBMigrator

DBtoDBMigrator는 SpringBatch를 이용하여 두 개의 데이터베이스에 접속하여 한 쪽의 데이터를 추출하여 다른 데이터베이스에 삽입하는 데이터 마이그레이션 어플리케이션입니다.
데이터 마이그레이션은 테이블단위로 수행됩니다.
이론상으로는 SpringFramework에서 지원하는 모든 RDBMS를 지원할 수 있지만 Oracle과 PostgreSQL기반으로 테스트 되었습니다.

아래 절차를 따라서 기본 구성을 설정하세요.

1. build.gradle에 적용하려는 RDBMS의 dependency를 추가하세요
2. application.txt를 application.yml로 변경하고 데이터베이스 접속정보를 설정하세요.
3. SourceDB의 target테이블을 DestinationDB에 동일하게 생성하세요.
4. Sample.java 수정하여 Target테이블에 대응하도록 합니다.
5. Source DB에 Spring Batch Metadata table을 생성하세요.
6. (선택) 데이터를 조작하려면 ItemProcessor.java를 수정하십시오.
7. target테이블의 내용과 일치하도록 BatchConfiguration.java의 provider, reader, writer를 수정하십시오.
8. (선택) 단 하나의 테이블만 마이그레이션 하려면 sample2패키지를 삭제하세요.
9. (선택) 하나의 테이블 마이그레이션과 하나의 잡을 매칭시키는 것을 권장합니다.
10. (선택) job이 두 개 이상일 때는 실행하고자 하는 잡을 program arguments에 지정합니다.

ex) ![image](https://github.com/nabyeongeun/DBtoDBMigrator/assets/99128141/fb97d848-20ff-4cd8-a47a-9ebab996d08e)

---

DBtoDBMigrator is a data migration application that connects to two databases using SpringBatch, extracts data from one side, and inserts it into the other database.
Data migration is executed table by table.
Theoretically, it can support all RDBMS supported by Spring Framework, but it has been tested based on Oracle and PostgreSQL.

Follow the steps below to set up a basic configuration.

1. Add the RDBMS dependencies you want to apply to build.gradle
2. Change application.txt to application.yml and set the database connection information.
3. Create the same target table in SourceDB in DestinationDB.
4. Modify Sample.java to correspond to Target table.
5. Create Spring Batch Metadata tables to source DB.
6. (Optional) Modify ItemProcessor.java to manipulate data.
7. Modify the provider, reader, and writer of BatchConfiguration.java to match the contents of the target table.
8. (Optional) To migrate just single table, delete the sample2 package
9. (Optional) It is recommended to match one table migration with one job.
10. (Optional) When there are more than one job, the job to be executed is specified in program arguments.

ex) ![image](https://github.com/nabyeongeun/DBtoDBMigrator/assets/99128141/fb97d848-20ff-4cd8-a47a-9ebab996d08e)
    

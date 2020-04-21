BEGIN TRANSACTION;

BEGIN TRY
    ${sqlScript}
END TRY

BEGIN CATCH
    ROLLBACK TRANSACTION;
    PRINT N'Errors occurred, rolling back changes.';

    DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10) + N'code:' + CAST(ERROR_NUMBER() AS VARCHAR)
        + CHAR(10) + N'message:' + ERROR_MESSAGE()
        + CHAR(10) + N'line:' + CAST(ERROR_LINE() AS VARCHAR)
        + CHAR(10) + N'procedure:' + ISNULL(ERROR_PROCEDURE(), '');
    DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY();
    DECLARE @ErrorState As Numeric = ERROR_STATE();
    RAISERROR ( @ErrorMessage, @ErrorSeverity, @ErrorState);
    RETURN;
END CATCH

BEGIN
    COMMIT TRANSACTION;
    PRINT N'Transaction success.';
END

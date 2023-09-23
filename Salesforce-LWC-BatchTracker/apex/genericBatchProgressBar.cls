/**
 * @description       : 
 * @author            : Sandeep Kumar
 * @group             : 
 * @last modified on  : 09-24-2023
 * @last modified by  : Sandeep Kumar
**/
public without sharing class genericBatchProgressBar {
    @AuraEnabled
    public static List<AsyncApexJob> getJobDetails(String jobId) {
        try {
            List<AsyncApexJob> asyncJobList = [
                SELECT Id, Status, JobItemsProcessed, TotalJobItems, NumberOfErrors 
                FROM AsyncApexJob 
                WHERE Id = :jobId
            ];
            return asyncJobList;
        } catch (Exception e) {
            throw new AuraHandledException(e.getMessage());
        }
    }
}
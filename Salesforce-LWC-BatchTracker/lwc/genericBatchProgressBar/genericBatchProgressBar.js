/**
 * @description       : Use this component to track batch completion called from UI/LWC
 * @author            : Sandeep Kumar
 * @group             : 
 * @last modified on  : 09-24-2023
 * @last modified by  : Sandeep Kumar
**/
import { LightningElement, api, track } from 'lwc';
import getJobDetails from '@salesforce/apex/genericBatchProgressBar.getJobDetails';
import { loadStyle } from 'lightning/platformResourceLoader';
import genericBatchProgressBarExtCss from '@salesforce/resourceUrl/genericBatchProgressBarExtCss';
export default class GenericBatchProgressBar extends LightningElement {
    @api batchJobId;
    executedPercentage;
    executedIndicator;
    executedBatch;
    totalBatch;
    isBatchCompleted = false;
    batchClassName;
    batchSize;
    disableExecuteBatch = false;
    initiated = false;
    resourceLoaded = false;
    isBatchErrorAvailable = false;
    errorPercentage;
    renderedCallback() {
        if (this.batchJobId != undefined && this.batchJobId != null && this.initiated == false) {
            this.disableExecuteBatch = true;
            this.initiated = true;
            this.getBatchStatus();
            this.refreshBatchOnInterval();
        }
        if (!this.resourceLoaded) {
            Promise.all([
                loadStyle(this, genericBatchProgressBarExtCss),
            ])
                .then(() => {
                    this.resourceLoaded = true;
                })
                .catch(error => {
                });
        }
    }
    @track batchStatus
    getBatchStatus() {
        getJobDetails({ jobId: this.batchJobId }).then(res => {
            this.batchStatus = res;
            if (res[0]) {
                this.totalBatch = res[0].TotalJobItems;
                if (this.totalBatch != 0) {
                    if (res[0].Status == 'Completed') {
                        this.isBatchCompleted = true;
                        clearInterval(this._interval);
                    }
                    this.executedPercentage = ((res[0].JobItemsProcessed / res[0].TotalJobItems) * 100).toFixed(2);
                    this.executedBatch = res[0].JobItemsProcessed;
                    var executedNumber = Number(this.executedPercentage);
                    this.executedIndicator = Math.floor(executedNumber);
                    if (this.isBatchCompleted && res[0].NumberOfErrors > 0) {
                        this.errorPercentage = ((res[0].NumberOfErrors / res[0].TotalJobItems) * 100).toFixed(2);
                        this.errorPercentage = Math.floor(Number(this.errorPercentage));
                        this.isBatchErrorAvailable = true;
                    }
                } else {
                    this.getBatchStatus();
                }
            }
        }).catch(err => {

        })
    }
    @track _interval;
    refreshBatchOnInterval() {
        this._interval = setInterval(() => {
            if (this.isBatchCompleted) {
                clearInterval(this._interval);

            } else {
                this.getBatchStatus();
            }
        }, 2000); //refersh view every time
    }
    handleComplete() {
        this.disableExecuteBatch = false;
        this.dispatchEvent(new CustomEvent('complete', {
            detail: this.batchStatus
        }))
    }
}

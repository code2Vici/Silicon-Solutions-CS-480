package siliconsolutions.cpp.phonecontactsbook;

/**
 * Created by naborp on 5/15/2017.
 */

public class ContactInfo {

    //private String id;
    private String contactName;
    private String phoneNumber;
    private String physicalAddress;
    private String emailAddress;
    private String linkedInField;
    private String instagramField;
    private String snapchatField;
    private String otherField;
    private String otherField2;

    public ContactInfo() {

    }

    public ContactInfo(String contactName, String phoneNumber, String physicalAddress, String emailAddress,
                       String linkedInField, String instagramField, String snapchatField, String otherField, String otherField2) {
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
        this.physicalAddress = physicalAddress;
        this.emailAddress = emailAddress;
        this.linkedInField = linkedInField;
        this.instagramField = instagramField;
        this.snapchatField = snapchatField;
        this.otherField = otherField;
        this.otherField2 = otherField2;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public String getLinkedInField() {
        return linkedInField;
    }

    public void setLinkedInField(String linkedInField) {
        this.linkedInField = linkedInField;
    }

    public String getInstagramField() {
        return instagramField;
    }

    public void setInstagramField(String instagramField) {
        this.instagramField = instagramField;
    }

    public String getSnapchatField() {
        return snapchatField;
    }

    public void setSnapchatField(String snapchatField) {
        this.snapchatField = snapchatField;
    }

    public String getOtherField() {
        return otherField;
    }

    public void setOtherField(String otherField) {
        this.otherField = otherField;
    }

    public String getOtherField2() {
        return otherField2;
    }

    public void setOtherField2(String otherField2) {
        this.otherField2 = otherField2;
    }



}

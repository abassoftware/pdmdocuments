package de.abas.pdmdocuments.infosystem.webservices.procad;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "/Part/pdmPartID", "/Part/pdmPartProductGroup", "/Part/pdmPartProductGroupAddon",
		"/Part/pdmPartType", "/Part/pdmPartCreateUser", "/Part/pdmPartCreateDate", "/Part/pdmPartRemark",
		"/Part/pdmPartLockUser", "/Part/pdmPartLockDate", "/Part/pdmPartState", "/Part/pdmPartModifyDate",
		"/Part/pdmPartModifyUser", "/Part/pdmPartANID", "/Part/pdmPartSourceSystemID", "/Part/pdmPartBOMUnit",
		"/Part/pdmPartBasicMaterialName", "/Part/pdmPartBasicMaterialNumber", "/Part/pdmPartDimensions",
		"/Part/pdmPartStandards", "/Part/pdmPartUsage", "/Part/pdmPartName", "/Part/pdmPartItemNumber",
		"/Part/pdmPartStateModifyUser", "/Part/pdmPartStateModifyDate", "/Part/pdmPartRemarkLong",
		"/Part/pdmPartSpecificationLong", "/Part/pdmPartRevision", "/Part/pdmPartVersion",
		"/Part/pdmPartRevisionChainAncestor", "/Part/pdmPartRevisionChainVersion", "/Part/pdmPartRevisionChainIndex",
		"/Part/pdmPartGUID", "/Part/pdmPartERPStateID", "/Part/pdmPartERPErrorMessage", "/Part/pdmPartERPTopStateID",
		"/Part/pdmPartERPTopErrorMessage", "/Part/pdmPartState[v]", "/Part/pdmPartRevision[v]",
		"/Part/pdmPartVersion[v]", "/Part/pdmPartERPErrorMessage[v]", "/Part/pdmPartERPTopErrorMessage[v]" })
public class Values {

//    @JsonProperty("/Part/pdmPartID")
//    private Integer partPdmPartID;
//    @JsonProperty("/Part/pdmPartProductGroup")
//    private String partPdmPartProductGroup;
//    @JsonProperty("/Part/pdmPartProductGroupAddon")
//    private String partPdmPartProductGroupAddon;
//    @JsonProperty("/Part/pdmPartType")
//    private String partPdmPartType;
//    @JsonProperty("/Part/pdmPartCreateUser")
//    private String partPdmPartCreateUser;
//    @JsonProperty("/Part/pdmPartCreateDate")
//    private String partPdmPartCreateDate;
//    @JsonProperty("/Part/pdmPartRemark")
//    private String partPdmPartRemark;
//    @JsonProperty("/Part/pdmPartLockUser")
//    private String partPdmPartLockUser;
//    @JsonProperty("/Part/pdmPartLockDate")
//    private String partPdmPartLockDate;
//    @JsonProperty("/Part/pdmPartState")
//    private Integer partPdmPartState;
//    @JsonProperty("/Part/pdmPartModifyDate")
//    private String partPdmPartModifyDate;
//    @JsonProperty("/Part/pdmPartModifyUser")
//    private String partPdmPartModifyUser;
//    @JsonProperty("/Part/pdmPartANID")
//    private String partPdmPartANID;
//    @JsonProperty("/Part/pdmPartSourceSystemID")
//    private String partPdmPartSourceSystemID;
//    @JsonProperty("/Part/pdmPartBOMUnit")
//    private String partPdmPartBOMUnit;
//    @JsonProperty("/Part/pdmPartBasicMaterialName")
//    private String partPdmPartBasicMaterialName;
//    @JsonProperty("/Part/pdmPartBasicMaterialNumber")
//    private String partPdmPartBasicMaterialNumber;
//    @JsonProperty("/Part/pdmPartDimensions")
//    private String partPdmPartDimensions;
//    @JsonProperty("/Part/pdmPartStandards")
//    private String partPdmPartStandards;
//    @JsonProperty("/Part/pdmPartUsage")
//    private String partPdmPartUsage;
//    @JsonProperty("/Part/pdmPartName")
//    private String partPdmPartName;
//    @JsonProperty("/Part/pdmPartItemNumber")
//    private String partPdmPartItemNumber;
//    @JsonProperty("/Part/pdmPartStateModifyUser")
//    private String partPdmPartStateModifyUser;
//    @JsonProperty("/Part/pdmPartStateModifyDate")
//    private String partPdmPartStateModifyDate;
//    @JsonProperty("/Part/pdmPartRemarkLong")
//    private String partPdmPartRemarkLong;
//    @JsonProperty("/Part/pdmPartSpecificationLong")
//    private String partPdmPartSpecificationLong;
//    @JsonProperty("/Part/pdmPartRevision")
//    private Integer partPdmPartRevision;
//    @JsonProperty("/Part/pdmPartVersion")
//    private Integer partPdmPartVersion;
//    @JsonProperty("/Part/pdmPartRevisionChainAncestor")
//    private Integer partPdmPartRevisionChainAncestor;
//    @JsonProperty("/Part/pdmPartRevisionChainVersion")
//    private Integer partPdmPartRevisionChainVersion;
//    @JsonProperty("/Part/pdmPartRevisionChainIndex")
//    private Integer partPdmPartRevisionChainIndex;
//    @JsonProperty("/Part/pdmPartGUID")
//    private String partPdmPartGUID;
//    @JsonProperty("/Part/pdmPartERPStateID")
//    private Integer partPdmPartERPStateID;
//    @JsonProperty("/Part/pdmPartERPErrorMessage")
//    private Integer partPdmPartERPErrorMessage;
//    @JsonProperty("/Part/pdmPartERPTopStateID")
//    private Integer partPdmPartERPTopStateID;
//    @JsonProperty("/Part/pdmPartERPTopErrorMessage")
//    private Integer partPdmPartERPTopErrorMessage;
//    @JsonProperty("/Part/pdmPartState[v]")
//    private String partPdmPartStateV;
//    @JsonProperty("/Part/pdmPartRevision[v]")
//    private String partPdmPartRevisionV;
//    @JsonProperty("/Part/pdmPartVersion[v]")
//    private String partPdmPartVersionV;
//    @JsonProperty("/Part/pdmPartERPErrorMessage[v]")
//    private String partPdmPartERPErrorMessageV;
//    @JsonProperty("/Part/pdmPartERPTopErrorMessage[v]")
//    private String partPdmPartERPTopErrorMessageV;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();
//
//    @JsonProperty("/Part/pdmPartID")
//    public Integer getPartPdmPartID() {
//        return partPdmPartID;
//    }
//
//    @JsonProperty("/Part/pdmPartID")
//    public void setPartPdmPartID(Integer partPdmPartID) {
//        this.partPdmPartID = partPdmPartID;
//    }
//
//    @JsonProperty("/Part/pdmPartProductGroup")
//    public String getPartPdmPartProductGroup() {
//        return partPdmPartProductGroup;
//    }
//
//    @JsonProperty("/Part/pdmPartProductGroup")
//    public void setPartPdmPartProductGroup(String partPdmPartProductGroup) {
//        this.partPdmPartProductGroup = partPdmPartProductGroup;
//    }
//
//    @JsonProperty("/Part/pdmPartProductGroupAddon")
//    public String getPartPdmPartProductGroupAddon() {
//        return partPdmPartProductGroupAddon;
//    }
//
//    @JsonProperty("/Part/pdmPartProductGroupAddon")
//    public void setPartPdmPartProductGroupAddon(String partPdmPartProductGroupAddon) {
//        this.partPdmPartProductGroupAddon = partPdmPartProductGroupAddon;
//    }
//
//    @JsonProperty("/Part/pdmPartType")
//    public String getPartPdmPartType() {
//        return partPdmPartType;
//    }
//
//    @JsonProperty("/Part/pdmPartType")
//    public void setPartPdmPartType(String partPdmPartType) {
//        this.partPdmPartType = partPdmPartType;
//    }
//
//    @JsonProperty("/Part/pdmPartCreateUser")
//    public String getPartPdmPartCreateUser() {
//        return partPdmPartCreateUser;
//    }
//
//    @JsonProperty("/Part/pdmPartCreateUser")
//    public void setPartPdmPartCreateUser(String partPdmPartCreateUser) {
//        this.partPdmPartCreateUser = partPdmPartCreateUser;
//    }
//
//    @JsonProperty("/Part/pdmPartCreateDate")
//    public String getPartPdmPartCreateDate() {
//        return partPdmPartCreateDate;
//    }
//
//    @JsonProperty("/Part/pdmPartCreateDate")
//    public void setPartPdmPartCreateDate(String partPdmPartCreateDate) {
//        this.partPdmPartCreateDate = partPdmPartCreateDate;
//    }
//
//    @JsonProperty("/Part/pdmPartRemark")
//    public String getPartPdmPartRemark() {
//        return partPdmPartRemark;
//    }
//
//    @JsonProperty("/Part/pdmPartRemark")
//    public void setPartPdmPartRemark(String partPdmPartRemark) {
//        this.partPdmPartRemark = partPdmPartRemark;
//    }
//
//    @JsonProperty("/Part/pdmPartLockUser")
//    public String getPartPdmPartLockUser() {
//        return partPdmPartLockUser;
//    }
//
//    @JsonProperty("/Part/pdmPartLockUser")
//    public void setPartPdmPartLockUser(String partPdmPartLockUser) {
//        this.partPdmPartLockUser = partPdmPartLockUser;
//    }
//
//    @JsonProperty("/Part/pdmPartLockDate")
//    public String getPartPdmPartLockDate() {
//        return partPdmPartLockDate;
//    }
//
//    @JsonProperty("/Part/pdmPartLockDate")
//    public void setPartPdmPartLockDate(String partPdmPartLockDate) {
//        this.partPdmPartLockDate = partPdmPartLockDate;
//    }
//
//    @JsonProperty("/Part/pdmPartState")
//    public Integer getPartPdmPartState() {
//        return partPdmPartState;
//    }
//
//    @JsonProperty("/Part/pdmPartState")
//    public void setPartPdmPartState(Integer partPdmPartState) {
//        this.partPdmPartState = partPdmPartState;
//    }
//
//    @JsonProperty("/Part/pdmPartModifyDate")
//    public String getPartPdmPartModifyDate() {
//        return partPdmPartModifyDate;
//    }
//
//    @JsonProperty("/Part/pdmPartModifyDate")
//    public void setPartPdmPartModifyDate(String partPdmPartModifyDate) {
//        this.partPdmPartModifyDate = partPdmPartModifyDate;
//    }
//
//    @JsonProperty("/Part/pdmPartModifyUser")
//    public String getPartPdmPartModifyUser() {
//        return partPdmPartModifyUser;
//    }
//
//    @JsonProperty("/Part/pdmPartModifyUser")
//    public void setPartPdmPartModifyUser(String partPdmPartModifyUser) {
//        this.partPdmPartModifyUser = partPdmPartModifyUser;
//    }
//
//    @JsonProperty("/Part/pdmPartANID")
//    public String getPartPdmPartANID() {
//        return partPdmPartANID;
//    }
//
//    @JsonProperty("/Part/pdmPartANID")
//    public void setPartPdmPartANID(String partPdmPartANID) {
//        this.partPdmPartANID = partPdmPartANID;
//    }
//
//    @JsonProperty("/Part/pdmPartSourceSystemID")
//    public String getPartPdmPartSourceSystemID() {
//        return partPdmPartSourceSystemID;
//    }
//
//    @JsonProperty("/Part/pdmPartSourceSystemID")
//    public void setPartPdmPartSourceSystemID(String partPdmPartSourceSystemID) {
//        this.partPdmPartSourceSystemID = partPdmPartSourceSystemID;
//    }
//
//    @JsonProperty("/Part/pdmPartBOMUnit")
//    public String getPartPdmPartBOMUnit() {
//        return partPdmPartBOMUnit;
//    }
//
//    @JsonProperty("/Part/pdmPartBOMUnit")
//    public void setPartPdmPartBOMUnit(String partPdmPartBOMUnit) {
//        this.partPdmPartBOMUnit = partPdmPartBOMUnit;
//    }
//
//    @JsonProperty("/Part/pdmPartBasicMaterialName")
//    public String getPartPdmPartBasicMaterialName() {
//        return partPdmPartBasicMaterialName;
//    }
//
//    @JsonProperty("/Part/pdmPartBasicMaterialName")
//    public void setPartPdmPartBasicMaterialName(String partPdmPartBasicMaterialName) {
//        this.partPdmPartBasicMaterialName = partPdmPartBasicMaterialName;
//    }
//
//    @JsonProperty("/Part/pdmPartBasicMaterialNumber")
//    public String getPartPdmPartBasicMaterialNumber() {
//        return partPdmPartBasicMaterialNumber;
//    }
//
//    @JsonProperty("/Part/pdmPartBasicMaterialNumber")
//    public void setPartPdmPartBasicMaterialNumber(String partPdmPartBasicMaterialNumber) {
//        this.partPdmPartBasicMaterialNumber = partPdmPartBasicMaterialNumber;
//    }
//
//    @JsonProperty("/Part/pdmPartDimensions")
//    public String getPartPdmPartDimensions() {
//        return partPdmPartDimensions;
//    }
//
//    @JsonProperty("/Part/pdmPartDimensions")
//    public void setPartPdmPartDimensions(String partPdmPartDimensions) {
//        this.partPdmPartDimensions = partPdmPartDimensions;
//    }
//
//    @JsonProperty("/Part/pdmPartStandards")
//    public String getPartPdmPartStandards() {
//        return partPdmPartStandards;
//    }
//
//    @JsonProperty("/Part/pdmPartStandards")
//    public void setPartPdmPartStandards(String partPdmPartStandards) {
//        this.partPdmPartStandards = partPdmPartStandards;
//    }
//
//    @JsonProperty("/Part/pdmPartUsage")
//    public String getPartPdmPartUsage() {
//        return partPdmPartUsage;
//    }
//
//    @JsonProperty("/Part/pdmPartUsage")
//    public void setPartPdmPartUsage(String partPdmPartUsage) {
//        this.partPdmPartUsage = partPdmPartUsage;
//    }
//
//    @JsonProperty("/Part/pdmPartName")
//    public String getPartPdmPartName() {
//        return partPdmPartName;
//    }
//
//    @JsonProperty("/Part/pdmPartName")
//    public void setPartPdmPartName(String partPdmPartName) {
//        this.partPdmPartName = partPdmPartName;
//    }
//
//    @JsonProperty("/Part/pdmPartItemNumber")
//    public String getPartPdmPartItemNumber() {
//        return partPdmPartItemNumber;
//    }
//
//    @JsonProperty("/Part/pdmPartItemNumber")
//    public void setPartPdmPartItemNumber(String partPdmPartItemNumber) {
//        this.partPdmPartItemNumber = partPdmPartItemNumber;
//    }
//
//    @JsonProperty("/Part/pdmPartStateModifyUser")
//    public String getPartPdmPartStateModifyUser() {
//        return partPdmPartStateModifyUser;
//    }
//
//    @JsonProperty("/Part/pdmPartStateModifyUser")
//    public void setPartPdmPartStateModifyUser(String partPdmPartStateModifyUser) {
//        this.partPdmPartStateModifyUser = partPdmPartStateModifyUser;
//    }
//
//    @JsonProperty("/Part/pdmPartStateModifyDate")
//    public String getPartPdmPartStateModifyDate() {
//        return partPdmPartStateModifyDate;
//    }
//
//    @JsonProperty("/Part/pdmPartStateModifyDate")
//    public void setPartPdmPartStateModifyDate(String partPdmPartStateModifyDate) {
//        this.partPdmPartStateModifyDate = partPdmPartStateModifyDate;
//    }
//
//    @JsonProperty("/Part/pdmPartRemarkLong")
//    public String getPartPdmPartRemarkLong() {
//        return partPdmPartRemarkLong;
//    }
//
//    @JsonProperty("/Part/pdmPartRemarkLong")
//    public void setPartPdmPartRemarkLong(String partPdmPartRemarkLong) {
//        this.partPdmPartRemarkLong = partPdmPartRemarkLong;
//    }
//
//    @JsonProperty("/Part/pdmPartSpecificationLong")
//    public String getPartPdmPartSpecificationLong() {
//        return partPdmPartSpecificationLong;
//    }
//
//    @JsonProperty("/Part/pdmPartSpecificationLong")
//    public void setPartPdmPartSpecificationLong(String partPdmPartSpecificationLong) {
//        this.partPdmPartSpecificationLong = partPdmPartSpecificationLong;
//    }
//
//    @JsonProperty("/Part/pdmPartRevision")
//    public Integer getPartPdmPartRevision() {
//        return partPdmPartRevision;
//    }
//
//    @JsonProperty("/Part/pdmPartRevision")
//    public void setPartPdmPartRevision(Integer partPdmPartRevision) {
//        this.partPdmPartRevision = partPdmPartRevision;
//    }
//
//    @JsonProperty("/Part/pdmPartVersion")
//    public Integer getPartPdmPartVersion() {
//        return partPdmPartVersion;
//    }
//
//    @JsonProperty("/Part/pdmPartVersion")
//    public void setPartPdmPartVersion(Integer partPdmPartVersion) {
//        this.partPdmPartVersion = partPdmPartVersion;
//    }
//
//    @JsonProperty("/Part/pdmPartRevisionChainAncestor")
//    public Integer getPartPdmPartRevisionChainAncestor() {
//        return partPdmPartRevisionChainAncestor;
//    }
//
//    @JsonProperty("/Part/pdmPartRevisionChainAncestor")
//    public void setPartPdmPartRevisionChainAncestor(Integer partPdmPartRevisionChainAncestor) {
//        this.partPdmPartRevisionChainAncestor = partPdmPartRevisionChainAncestor;
//    }
//
//    @JsonProperty("/Part/pdmPartRevisionChainVersion")
//    public Integer getPartPdmPartRevisionChainVersion() {
//        return partPdmPartRevisionChainVersion;
//    }
//
//    @JsonProperty("/Part/pdmPartRevisionChainVersion")
//    public void setPartPdmPartRevisionChainVersion(Integer partPdmPartRevisionChainVersion) {
//        this.partPdmPartRevisionChainVersion = partPdmPartRevisionChainVersion;
//    }
//
//    @JsonProperty("/Part/pdmPartRevisionChainIndex")
//    public Integer getPartPdmPartRevisionChainIndex() {
//        return partPdmPartRevisionChainIndex;
//    }
//
//    @JsonProperty("/Part/pdmPartRevisionChainIndex")
//    public void setPartPdmPartRevisionChainIndex(Integer partPdmPartRevisionChainIndex) {
//        this.partPdmPartRevisionChainIndex = partPdmPartRevisionChainIndex;
//    }
//
//    @JsonProperty("/Part/pdmPartGUID")
//    public String getPartPdmPartGUID() {
//        return partPdmPartGUID;
//    }
//
//    @JsonProperty("/Part/pdmPartGUID")
//    public void setPartPdmPartGUID(String partPdmPartGUID) {
//        this.partPdmPartGUID = partPdmPartGUID;
//    }
//
//    @JsonProperty("/Part/pdmPartERPStateID")
//    public Integer getPartPdmPartERPStateID() {
//        return partPdmPartERPStateID;
//    }
//
//    @JsonProperty("/Part/pdmPartERPStateID")
//    public void setPartPdmPartERPStateID(Integer partPdmPartERPStateID) {
//        this.partPdmPartERPStateID = partPdmPartERPStateID;
//    }
//
//    @JsonProperty("/Part/pdmPartERPErrorMessage")
//    public Integer getPartPdmPartERPErrorMessage() {
//        return partPdmPartERPErrorMessage;
//    }
//
//    @JsonProperty("/Part/pdmPartERPErrorMessage")
//    public void setPartPdmPartERPErrorMessage(Integer partPdmPartERPErrorMessage) {
//        this.partPdmPartERPErrorMessage = partPdmPartERPErrorMessage;
//    }
//
//    @JsonProperty("/Part/pdmPartERPTopStateID")
//    public Integer getPartPdmPartERPTopStateID() {
//        return partPdmPartERPTopStateID;
//    }
//
//    @JsonProperty("/Part/pdmPartERPTopStateID")
//    public void setPartPdmPartERPTopStateID(Integer partPdmPartERPTopStateID) {
//        this.partPdmPartERPTopStateID = partPdmPartERPTopStateID;
//    }
//
//    @JsonProperty("/Part/pdmPartERPTopErrorMessage")
//    public Integer getPartPdmPartERPTopErrorMessage() {
//        return partPdmPartERPTopErrorMessage;
//    }
//
//    @JsonProperty("/Part/pdmPartERPTopErrorMessage")
//    public void setPartPdmPartERPTopErrorMessage(Integer partPdmPartERPTopErrorMessage) {
//        this.partPdmPartERPTopErrorMessage = partPdmPartERPTopErrorMessage;
//    }
//
//    @JsonProperty("/Part/pdmPartState[v]")
//    public String getPartPdmPartStateV() {
//        return partPdmPartStateV;
//    }
//
//    @JsonProperty("/Part/pdmPartState[v]")
//    public void setPartPdmPartStateV(String partPdmPartStateV) {
//        this.partPdmPartStateV = partPdmPartStateV;
//    }
//
//    @JsonProperty("/Part/pdmPartRevision[v]")
//    public String getPartPdmPartRevisionV() {
//        return partPdmPartRevisionV;
//    }
//
//    @JsonProperty("/Part/pdmPartRevision[v]")
//    public void setPartPdmPartRevisionV(String partPdmPartRevisionV) {
//        this.partPdmPartRevisionV = partPdmPartRevisionV;
//    }
//
//    @JsonProperty("/Part/pdmPartVersion[v]")
//    public String getPartPdmPartVersionV() {
//        return partPdmPartVersionV;
//    }
//
//    @JsonProperty("/Part/pdmPartVersion[v]")
//    public void setPartPdmPartVersionV(String partPdmPartVersionV) {
//        this.partPdmPartVersionV = partPdmPartVersionV;
//    }
//
//    @JsonProperty("/Part/pdmPartERPErrorMessage[v]")
//    public String getPartPdmPartERPErrorMessageV() {
//        return partPdmPartERPErrorMessageV;
//    }
//
//    @JsonProperty("/Part/pdmPartERPErrorMessage[v]")
//    public void setPartPdmPartERPErrorMessageV(String partPdmPartERPErrorMessageV) {
//        this.partPdmPartERPErrorMessageV = partPdmPartERPErrorMessageV;
//    }
//
//    @JsonProperty("/Part/pdmPartERPTopErrorMessage[v]")
//    public String getPartPdmPartERPTopErrorMessageV() {
//        return partPdmPartERPTopErrorMessageV;
//    }
//
//    @JsonProperty("/Part/pdmPartERPTopErrorMessage[v]")
//    public void setPartPdmPartERPTopErrorMessageV(String partPdmPartERPTopErrorMessageV) {
//        this.partPdmPartERPTopErrorMessageV = partPdmPartERPTopErrorMessageV;
//    }

	@JsonAnyGetter
	public Map<String, java.lang.Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, java.lang.Object value) {
		this.additionalProperties.put(name, value);
	}

}

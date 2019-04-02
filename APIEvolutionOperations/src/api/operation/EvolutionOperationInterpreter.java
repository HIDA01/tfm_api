package api.operation;

import api.fm.FMView;
import api.operation.feature.FeatureAddOperation;
import api.operation.feature.FeatureChangeVariationTypeOperation;
import api.operation.feature.FeatureCreateDefaultChildGroupOperation;
import api.operation.feature.FeatureDetachOperation;
import api.operation.feature.FeatureMoveOperation;
import api.operation.feature.FeatureRemoveOperation;
import api.operation.feature.FeatureRenameOperation;
import api.operation.fm.ChangeRootFeatureOperation;
import api.operation.fm.CreateFeatureOperation;
import api.operation.fm.CreateGroupOperation;
import api.operation.group.GroupAddOperation;
import api.operation.group.GroupChangeVariationTypeOperation;
import api.operation.group.GroupDetachOperation;
import api.operation.group.GroupMoveOperation;
import api.operation.group.GroupRemoveOperation;
import api.tfm.TFMView;
import fm.Feature;
import fm.Group;
import fm.GroupVariationType;
import tfm.util.TemporalPoint;

public class EvolutionOperationInterpreter {

	protected FMView fmView;
	protected TFMView tfmView;

	public EvolutionOperationInterpreter(FMView fmView) {
		this.fmView = fmView;
	}

	public EvolutionOperationInterpreter(TFMView tfmView) {
		this.tfmView = tfmView;
	}

	public EvolutionOperation replay(EvolutionOperation operation) {
		if (operation == null) {
			return null;
		}
		
		EvolutionOperation clone = (EvolutionOperation) operation.clone();
		execute(clone);
		return clone;
	}
	
	public void execute(EvolutionOperation operation) {

		// TODO catch whether the operation has already been executed? May be
		// problematic for replay/transform format?

		if (operation instanceof ChangeRootFeatureOperation) {
			doExecute((ChangeRootFeatureOperation) operation);
		} else if (operation instanceof CreateFeatureOperation) {
			doExecute((CreateFeatureOperation) operation);
		} else if (operation instanceof CreateGroupOperation) {
			doExecute((CreateGroupOperation) operation);
		} else if (operation instanceof FeatureAddOperation) {
			doExecute((FeatureAddOperation) operation);
		} else if (operation instanceof FeatureChangeVariationTypeOperation) {
			doExecute((FeatureChangeVariationTypeOperation) operation);
		} else if (operation instanceof FeatureCreateDefaultChildGroupOperation) {
			doExecute((FeatureCreateDefaultChildGroupOperation) operation);
		} else if (operation instanceof FeatureDetachOperation) {
			doExecute((FeatureDetachOperation) operation);
		} else if (operation instanceof FeatureMoveOperation) {
			doExecute((FeatureMoveOperation) operation);
		} else if (operation instanceof FeatureRemoveOperation) {
			doExecute((FeatureRemoveOperation) operation);
		} else if (operation instanceof FeatureRenameOperation) {
			doExecute((FeatureRenameOperation) operation);
		} else if (operation instanceof GroupAddOperation) {
			doExecute((GroupAddOperation) operation);
		} else if (operation instanceof GroupChangeVariationTypeOperation) {
			doExecute((GroupChangeVariationTypeOperation) operation);
		} else if (operation instanceof GroupDetachOperation) {
			doExecute((GroupDetachOperation) operation);
		} else if (operation instanceof GroupMoveOperation) {
			doExecute((GroupMoveOperation) operation);
		} else if (operation instanceof GroupRemoveOperation) {
			doExecute((GroupRemoveOperation) operation);
		}

		operation.setHasBeenExecuted(true);
	}

	public void undo(EvolutionOperation operation) {

		// TODO code for undoing

		operation.setHasBeenExecuted(false);
	}
	
	protected void doExecute(ChangeRootFeatureOperation operation) {
		if (fmView != null) {
			operation.setOldRootFeature(fmView.getRootFeature());
			fmView.setRootFeature(operation.getNewRootFeature());
		} else if (tfmView != null) {
			operation.setOldRootFeature(tfmView.getRootFeatureAt(operation.getOperationDate()));
			tfmView.setRootFeatureAt(operation.getNewRootFeature(), operation.getOperationDate());
		}
	}
	
	protected void doExecute(CreateFeatureOperation operation) {
		Feature feature = null;
		
		if (fmView != null) {
			feature = fmView.createFeature(operation.getFeatureName());
		} else if (tfmView != null) {
			feature = tfmView.createFeatureAt(operation.getFeatureName(), operation.getOperationDate());
		}
		
		operation.setFeature(feature);
	}

	protected void doExecute(CreateGroupOperation operation) {
		Group group = null;
		if (fmView != null) {
			group = fmView.createGroup();
		} else if (tfmView != null) {
			group = tfmView.createGroupAt(operation.getOperationDate());
		}
		operation.setGroup(group);
	}
	
	protected void doExecute(FeatureAddOperation operation) {
		Group targetGroup = operation.getNewParentGroup();
		if (fmView != null) {
			if (targetGroup != null) {
				fmView.addFeature(operation.getFeature(), targetGroup);
			}
			else {
				fmView.addFeature(operation.getFeature(), operation.getNewParentFeature());
			}
		} else if (tfmView != null) {
			if (targetGroup != null) {
				tfmView.addFeatureAt(operation.getFeature(), targetGroup, operation.getOperationDate());
			}
			else {
				tfmView.addFeatureAt(operation.getFeature(), operation.getNewParentFeature(), operation.getOperationDate());
			}
		}
	}

	private void doExecute(FeatureRenameOperation operation) {
		if (fmView != null) {
			operation.setOldName(fmView.getFeatureName(operation.getFeature()));
			fmView.setFeatureName(operation.getFeature(), operation.getNewName());
		} else if (tfmView != null) {
			operation.setOldName(tfmView.getFeatureNameAt(operation.getFeature(), operation.getOperationDate()));
			tfmView.setFeatureNameAt(operation.getFeature(), operation.getNewName(), operation.getOperationDate());
		}
	}

	private void doExecute(FeatureRemoveOperation operation) {
		Feature feature = operation.getFeature();
		if (fmView != null) {
			operation.setOldParentFeature(fmView.getFeatureParentFeature(feature));
			operation.setOldParentGroup(fmView.getFeatureParentGroup(feature));
			fmView.removeFeature(feature);
		} else if (tfmView != null) {
			TemporalPoint operationDate = operation.getOperationDate();
			
			operation.setOldParentFeature(tfmView.getFeatureParentFeatureAt(feature, operationDate));
			operation.setOldParentGroup(tfmView.getFeatureParentGroupAt(feature, operationDate));
			tfmView.removeFeatureAt(feature, operationDate);
		}
	}

	private void doExecute(FeatureMoveOperation operation) {
		Feature feature = operation.getFeature();
		Group targetGroup = operation.getNewParentGroup();
		Feature targetFeature = operation.getNewParentFeature();
		
		if (fmView != null) {
			operation.setOldParentFeature(fmView.getFeatureParentFeature(feature));
			operation.setOldParentGroup(fmView.getFeatureParentGroup(feature));
			
			if (targetGroup != null) {
				fmView.addFeature(feature, targetGroup);
			}
			else {
				fmView.addFeature(feature, targetFeature);
			}
		} else if (tfmView != null) {
			TemporalPoint operationDate = operation.getOperationDate();
			
			operation.setOldParentFeature(tfmView.getFeatureParentFeatureAt(feature, operationDate));
			operation.setOldParentGroup(tfmView.getFeatureParentGroupAt(feature, operationDate));
			
			if (targetGroup != null) {
				tfmView.addFeatureAt(feature, targetGroup, operationDate);
			}
			else {
				tfmView.addFeatureAt(feature, targetFeature, operationDate);
			}
		}
	}

	private void doExecute(FeatureDetachOperation operation) {
		// TODO not possible with API at the current state.
		throw new UnsupportedOperationException("Detaching features is currently not supported by the API");
	}

	private void doExecute(FeatureCreateDefaultChildGroupOperation operation) {
		if (fmView != null) {
			operation.setNewDefaultChildGroup(fmView.createDefaultChildGroup(operation.getFeature()));
		} else if (tfmView != null) {
			operation.setNewDefaultChildGroup(tfmView.createDefaultChildGroupAt(operation.getFeature(), operation.getOperationDate()));
		}
	}

	private void doExecute(FeatureChangeVariationTypeOperation operation) {
		Feature feature = operation.getFeature();
		
		if (fmView != null) {
			operation.setOldVariationType(fmView.getFeatureVariationType(feature));
			fmView.setFeatureVariationType(feature, operation.getNewVariationType());
		} else if (tfmView != null) {
			TemporalPoint operationDate = operation.getOperationDate();
			operation.setOldVariationType(tfmView.getFeatureVariationTypeAt(feature, operationDate));
			tfmView.setFeatureVariationTypeAt(feature, operation.getNewVariationType(), operationDate);
		}
	}

	private void doExecute(GroupRemoveOperation operation) {
		Group group = operation.getGroup();
		
		if (fmView != null) {
			operation.setOldParentFeature(fmView.getGroupParentFeature(group));
			fmView.removeGroup(group);
		} else if (tfmView != null) {
			TemporalPoint operationDate = operation.getOperationDate();
			operation.setOldParentFeature(tfmView.getGroupParentFeatureAt(group, operationDate));
			tfmView.removeGroupAt(group, operationDate);
		}
	}

	private void doExecute(GroupMoveOperation operation) {
		Group group = operation.getGroup();
		Feature newParentFeature = operation.getNewParentFeature();
		
		if (fmView != null) {
			operation.setOldParentFeature(fmView.getGroupParentFeature(group));
			fmView.moveGroup(group, newParentFeature);
		} else if (tfmView != null) {
			TemporalPoint operationDate = operation.getOperationDate();
			operation.setOldParentFeature(tfmView.getGroupParentFeatureAt(group, operationDate));
			tfmView.moveGroupAt(group, newParentFeature, operationDate);
		}
	}

	private void doExecute(GroupDetachOperation operation) {
		// TODO not possible with API at the current state.
		throw new UnsupportedOperationException("Detaching groups  is currently not supported by the API");
	}

	private void doExecute(GroupChangeVariationTypeOperation operation) {
		Group group = operation.getGroup();
		GroupVariationType newGroupVariationType = operation.getNewVariationType();
		
		if (fmView != null) {
			operation.setOldVariationType(fmView.getGroupVariationType(group));
			fmView.setGroupVariationType(group, newGroupVariationType);
		} else if (tfmView != null) {
			TemporalPoint operationDate = operation.getOperationDate();
			operation.setOldVariationType(tfmView.getGroupVariationTypeAt(group, operationDate));
			tfmView.setGroupVariationTypeAt(group, newGroupVariationType, operationDate);
		}
	}

	private void doExecute(GroupAddOperation operation) {
		Group group = operation.getGroup();
		Feature newParentFeature = operation.getNewParentFeature();
		
		if (fmView != null) {
			fmView.addGroup(group, newParentFeature);
		} else if (tfmView != null) {
			tfmView.addGroupAt(group, newParentFeature, operation.getOperationDate());
		}
	}
}

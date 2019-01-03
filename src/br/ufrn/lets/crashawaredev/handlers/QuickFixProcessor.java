package br.ufrn.lets.crashawaredev.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public class QuickFixProcessor implements IQuickFixProcessor {

	// TODO: https://stackoverflow.com/questions/10618164/how-do-i-show-quick-fixes-when-i-hover-over-an-error-in-my-custom-editor
	
	@Override
	public IJavaCompletionProposal[] getCorrections(IInvocationContext context, IProblemLocation[] arg1)
			throws CoreException {
		List<IJavaCompletionProposal> proposals = new ArrayList<>();
        addProposal(context, proposals);
        return proposals.toArray(new IJavaCompletionProposal[proposals.size()]);
	}

	@Override
	public boolean hasCorrections(ICompilationUnit arg0, int arg1) {
		return true;
	}
	
	private void addProposal(IInvocationContext context, List<IJavaCompletionProposal> proposals) {
        proposals.add(new IJavaCompletionProposal() {
			
			@Override
			public Point getSelection(IDocument arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Image getImage() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getDisplayString() {
				return "SHOW";
			}
			
			@Override
			public IContextInformation getContextInformation() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getAdditionalProposalInfo() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void apply(IDocument arg0) {
				System.out.print("@@@@@@@@@@@@!!!!!!!!!!@@@@@@@@@@@@");				
			}
			
			@Override
			public int getRelevance() {
				return 999;
			}
		});
    }

}

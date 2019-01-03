package br.ufrn.lets.crashawaredev.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickAssistProcessor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public class QuickAssistProcessor implements IQuickAssistProcessor {
	
	@Override
	public IJavaCompletionProposal[] getAssists(IInvocationContext context, IProblemLocation[] locations) throws CoreException {
        List<IJavaCompletionProposal> proposals = new ArrayList<>();
        addProposal(context, proposals);
        return proposals.toArray(new IJavaCompletionProposal[proposals.size()]);
	}

	@Override
	public boolean hasAssists(IInvocationContext arg0) throws CoreException {
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
				System.out.print("@@@@@@@@@@@@@@@@@@@@@@@@@");				
			}
			
			@Override
			public int getRelevance() {
				return -99;
			}
		});
    }


}

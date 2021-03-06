/*
 * Copyright (c) 2003, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jdk.javadoc.internal.doclets.formats.html;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import jdk.javadoc.internal.doclets.formats.html.markup.ContentBuilder;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlTree;
import jdk.javadoc.internal.doclets.formats.html.markup.Table;
import jdk.javadoc.internal.doclets.formats.html.markup.TableHeader;
import jdk.javadoc.internal.doclets.toolkit.AnnotationTypeRequiredMemberWriter;
import jdk.javadoc.internal.doclets.toolkit.Content;
import jdk.javadoc.internal.doclets.toolkit.MemberSummaryWriter;


/**
 * Writes annotation type required member documentation in HTML format.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 * @author Bhavesh Patel (Modified)
 */
public class AnnotationTypeRequiredMemberWriterImpl extends AbstractMemberWriter
    implements AnnotationTypeRequiredMemberWriter, MemberSummaryWriter {

    /**
     * Construct a new AnnotationTypeRequiredMemberWriterImpl.
     *
     * @param writer         the writer that will write the output.
     * @param annotationType the AnnotationType that holds this member.
     */
    public AnnotationTypeRequiredMemberWriterImpl(SubWriterHolderWriter writer,
            TypeElement annotationType) {
        super(writer, annotationType);
    }

    /**
     * {@inheritDoc}
     */
    public Content getMemberSummaryHeader(TypeElement typeElement,
            Content memberSummaryTree) {
        memberSummaryTree.add(
                MarkerComments.START_OF_ANNOTATION_TYPE_REQUIRED_MEMBER_SUMMARY);
        Content memberTree = new ContentBuilder();
        writer.addSummaryHeader(this, typeElement, memberTree);
        return memberTree;
    }

    /**
     * {@inheritDoc}
     */
    public Content getMemberTreeHeader() {
        return writer.getMemberTreeHeader();
    }

    /**
     * {@inheritDoc}
     */
    public void addMemberTree(Content memberSummaryTree, Content memberTree) {
        writer.addMemberTree(HtmlStyle.memberSummary, memberSummaryTree, memberTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addAnnotationDetailsMarker(Content memberDetails) {
        memberDetails.add(MarkerComments.START_OF_ANNOTATION_TYPE_DETAILS);
    }

    /**
     * {@inheritDoc}
     */
    public Content getAnnotationDetailsTreeHeader(TypeElement te) {
        Content memberDetailsTree = new ContentBuilder();
        if (!writer.printedAnnotationHeading) {
            Content heading = HtmlTree.HEADING(Headings.TypeDeclaration.DETAILS_HEADING,
                    contents.annotationTypeDetailsLabel);
            memberDetailsTree.add(heading);
            memberDetailsTree.add(links.createAnchor(
                    SectionName.ANNOTATION_TYPE_ELEMENT_DETAIL));
            writer.printedAnnotationHeading = true;
        }
        return memberDetailsTree;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Content getAnnotationDocTreeHeader(Element member, Content annotationDetailsTree) {
        String simpleName = name(member);
        Content annotationDocTree = new ContentBuilder();
        Content heading = new HtmlTree(Headings.TypeDeclaration.MEMBER_HEADING);
        heading.add(simpleName);
        annotationDocTree.add(heading);
        annotationDocTree.add(links.createAnchor(
                simpleName + utils.signature((ExecutableElement) member)));
        return HtmlTree.SECTION(HtmlStyle.detail, annotationDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public Content getSignature(Element member) {
        return new MemberSignature(member)
                .addType(getType(member))
                .toContent();
    }

    /**
     * {@inheritDoc}
     */
    public void addDeprecated(Element member, Content annotationDocTree) {
        addDeprecatedInfo(member, annotationDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addComments(Element member, Content annotationDocTree) {
        addComment(member, annotationDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addTags(Element member, Content annotationDocTree) {
        writer.addTagsInfo(member, annotationDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public Content getAnnotationDetails(Content annotationDetailsTreeHeader, Content annotationDetailsTree) {
        Content annotationDetails = new ContentBuilder(annotationDetailsTreeHeader, annotationDetailsTree);
        return getMemberTree(HtmlTree.SECTION(HtmlStyle.memberDetails, annotationDetails));
    }

    /**
     * {@inheritDoc}
     */
    public Content getAnnotationDoc(Content annotationDocTree) {
        return getMemberTree(annotationDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryLabel(Content memberTree) {
        Content label = HtmlTree.HEADING(Headings.TypeDeclaration.SUMMARY_HEADING,
                contents.annotateTypeRequiredMemberSummaryLabel);
        memberTree.add(label);
    }

    /**
     * Get the caption for the summary table.
     * @return the caption
     */
    // Overridden by AnnotationTypeOptionalMemberWriterImpl
    protected Content getCaption() {
        return contents.getContent("doclet.Annotation_Type_Required_Members");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableHeader getSummaryTableHeader(Element member) {
        return new TableHeader(contents.modifierAndTypeLabel,
                contents.annotationTypeRequiredMemberLabel, contents.descriptionLabel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Table createSummaryTable() {
        return new Table(HtmlStyle.memberSummary)
                .setCaption(getCaption())
                .setHeader(getSummaryTableHeader(typeElement))
                .setRowScopeColumn(1)
                .setColumnStyles(HtmlStyle.colFirst, HtmlStyle.colSecond, HtmlStyle.colLast);
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryAnchor(TypeElement typeElement, Content memberTree) {
        memberTree.add(links.createAnchor(
                SectionName.ANNOTATION_TYPE_REQUIRED_ELEMENT_SUMMARY));
    }

    /**
     * {@inheritDoc}
     */
    public void addInheritedSummaryAnchor(TypeElement typeElement, Content inheritedTree) {
    }

    /**
     * {@inheritDoc}
     */
    public void addInheritedSummaryLabel(TypeElement typeElement, Content inheritedTree) {
    }

    /**
     * {@inheritDoc}
     */
    protected void addSummaryLink(LinkInfoImpl.Kind context, TypeElement typeElement, Element member,
            Content tdSummary) {
        Content memberLink = HtmlTree.SPAN(HtmlStyle.memberNameLink,
                writer.getDocLink(context, member, name(member), false));
        Content code = HtmlTree.CODE(memberLink);
        tdSummary.add(code);
    }

    /**
     * {@inheritDoc}
     */
    protected void addInheritedSummaryLink(TypeElement typeElement,
            Element member, Content linksTree) {
        //Not applicable.
    }

    /**
     * {@inheritDoc}
     */
    protected void addSummaryType(Element member, Content tdSummaryType) {
        addModifierAndType(member, getType(member), tdSummaryType);
    }

    /**
     * {@inheritDoc}
     */
    protected Content getDeprecatedLink(Element member) {
        String name = utils.getFullyQualifiedName(member) + "." + member.getSimpleName();
        return writer.getDocLink(LinkInfoImpl.Kind.MEMBER, member, name);
    }

    private TypeMirror getType(Element member) {
        return utils.isExecutableElement(member)
                ? utils.getReturnType((ExecutableElement) member)
                : member.asType();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goobar.cextractor.dependencytracker;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

/**
 *
 * @author goobar
 */
public class DependencyVisitor implements AnnotationVisitor, SignatureVisitor,
	ClassVisitor, FieldVisitor, MethodVisitor
{

	private Map<String, Integer> current;

	private final Set<String> dependencies = new HashSet<String>();

	private final Map<String, Map<String, Integer>> groups = new HashMap<String, Map<String, Integer>>();

	// SignatureVisitor
	private String signatureClassName;

	/**
	 * @return all dependencies collected by this visitor
	 */
	public Set<String> getDependencies()
	{
		return Collections.unmodifiableSet(dependencies);
	}

	// ClassVisitor
	@Override
	public void visit(final int version, final int access,
		final String name, final String signature,
		final String superName, final String[] interfaces)
	{
		String p = getGroupKey(name);
		current = groups.get(p);
		if (current == null)
		{
			current = new HashMap<String, Integer>();
			groups.put(p, current);
		}

		if (signature == null)
		{
			addInternalName(superName);
			addInternalNames(interfaces);
		}
		else
		{
			addSignature(signature);
		}
	}

	// AnnotationVisitor
	@Override
	public void visit(final String name, final Object value)
	{
		if (value instanceof Type)
		{
			addType((Type) value);
		}
	}

	@Override
	public AnnotationVisitor visitAnnotation(final String desc,
		final boolean visible)
	{
		addDesc(desc);
		return this;
	}

	@Override
	public AnnotationVisitor visitAnnotation(final String name,
		final String desc)
	{
		addDesc(desc);
		return this;
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault()
	{
		return this;
	}

	@Override
	public AnnotationVisitor visitArray(final String name)
	{
		return this;
	}

	@Override
	public SignatureVisitor visitArrayType()
	{
		return this;
	}

	@Override
	public void visitAttribute(final Attribute attr)
	{
	}

	@Override
	public void visitBaseType(final char descriptor)
	{
	}

	@Override
	public SignatureVisitor visitClassBound()
	{
		return this;
	}

	@Override
	public void visitClassType(final String name)
	{
		signatureClassName = name;
		addInternalName(name);
	}

	@Override
	public void visitCode()
	{
	}

	// common
	@Override
	public void visitEnd()
	{
	}

	@Override
	public void visitEnum(final String name, final String desc,
		final String value)
	{
		addDesc(desc);
	}

	@Override
	public SignatureVisitor visitExceptionType()
	{
		return this;
	}

	@Override
	public FieldVisitor visitField(final int access, final String name,
		final String desc, final String signature, final Object value)
	{
		if (signature == null)
		{
			addDesc(desc);
		}
		else
		{
			addTypeSignature(signature);
		}
		if (value instanceof Type)
		{
			addType((Type) value);
		}
		return this;
	}

	@Override
	public void visitFieldInsn(final int opcode, final String owner,
		final String name, final String desc)
	{
		addInternalName(owner);
		addDesc(desc);
	}

	@Override
	public void visitFormalTypeParameter(final String name)
	{
	}

	@Override
	public void visitFrame(final int type, final int nLocal,
		final Object[] local, final int nStack, final Object[] stack)
	{
	}

	@Override
	public void visitIincInsn(final int var, final int increment)
	{
	}

	@Override
	public void visitInnerClass(final String name, final String outerName,
		final String innerName, final int access)
	{
	}

	@Override
	public void visitInnerClassType(final String name)
	{
		signatureClassName = signatureClassName + "$" + name;
		addInternalName(signatureClassName);
	}

	@Override
	public void visitInsn(final int opcode)
	{
	}

	@Override
	public SignatureVisitor visitInterface()
	{
		return this;
	}

	@Override
	public SignatureVisitor visitInterfaceBound()
	{
		return this;
	}

	@Override
	public void visitIntInsn(final int opcode, final int operand)
	{
	}

	@Override
	public void visitJumpInsn(final int opcode, final Label label)
	{
	}

	@Override
	public void visitLabel(final Label label)
	{
	}

	@Override
	public void visitLdcInsn(final Object cst)
	{
		if (cst instanceof Type)
		{
			addType((Type) cst);
		}
	}

	@Override
	public void visitLineNumber(final int line, final Label start)
	{
	}

	@Override
	public void visitLocalVariable(final String name, final String desc,
		final String signature, final Label start, final Label end,
		final int index)
	{
		addTypeSignature(signature);
	}

	@Override
	public void visitLookupSwitchInsn(final Label dflt, final int[] keys,
		final Label[] labels)
	{
	}

	@Override
	public void visitMaxs(final int maxStack, final int maxLocals)
	{
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name,
		final String desc, final String signature,
		final String[] exceptions)
	{
		if (signature == null)
		{
			addMethodDesc(desc);
		}
		else
		{
			addSignature(signature);
		}
		addInternalNames(exceptions);
		return this;
	}

	@Override
	public void visitMethodInsn(final int opcode, final String owner,
		final String name, final String desc)
	{
		addInternalName(owner);
		addMethodDesc(desc);
	}

	@Override
	public void visitMultiANewArrayInsn(final String desc, final int dims)
	{
		addDesc(desc);
	}

	@Override
	public void visitOuterClass(final String owner, final String name,
		final String desc)
	{
	}

	// MethodVisitor
	@Override
	public AnnotationVisitor visitParameterAnnotation(final int parameter,
		final String desc, final boolean visible)
	{
		addDesc(desc);
		return this;
	}

	@Override
	public SignatureVisitor visitParameterType()
	{
		return this;
	}

	@Override
	public SignatureVisitor visitReturnType()
	{
		return this;
	}

	@Override
	public void visitSource(final String source, final String debug)
	{
	}

	@Override
	public SignatureVisitor visitSuperclass()
	{
		return this;
	}

	@Override
	public void visitTableSwitchInsn(final int min, final int max,
		final Label dflt, final Label[] labels)
	{
	}

	@Override
	public void visitTryCatchBlock(final Label start, final Label end,
		final Label handler, final String type)
	{
		addInternalName(type);
	}

	@Override
	public void visitTypeArgument()
	{
	}

	@Override
	public SignatureVisitor visitTypeArgument(final char wildcard)
	{
		return this;
	}

	@Override
	public void visitTypeInsn(final int opcode, final String type)
	{
		addType(Type.getObjectType(type));
	}

	@Override
	public void visitTypeVariable(final String name)
	{
	}

	@Override
	public void visitVarInsn(final int opcode, final int var)
	{
	}

	private void addDesc(final String desc)
	{
		addType(Type.getType(desc));
	}

	private void addInternalName(final String name)
	{
		addType(Type.getObjectType(name));
	}

	private void addInternalNames(final String[] names)
	{
		for (int i = 0; names != null && i < names.length; i++)
		{
			addInternalName(names[i]);
		}
	}

	private void addMethodDesc(final String desc)
	{
		addType(Type.getReturnType(desc));
		Type[] types = Type.getArgumentTypes(desc);
		for (int i = 0; i < types.length; i++)
		{
			addType(types[i]);
		}
	}

	private void addName(final String name)
	{
		if (name == null)
		{
			return;
		}
		String p = getGroupKey(name);
		if (current.containsKey(p))
		{
			current.put(p, current.get(p) + 1);
		}
		else
		{
			current.put(p, 1);
		}
	}

	private void addSignature(final String signature)
	{
		if (signature != null)
		{
			new SignatureReader(signature).accept(this);
		}
	}

	private void addType(final Type t)
	{
		switch (t.getSort())
		{
			case Type.ARRAY:
				addType(t.getElementType());
				break;
			case Type.OBJECT:
				addName(t.getInternalName());
				break;
		}
	}

	private void addTypeSignature(final String signature)
	{
		if (signature != null)
		{
			new SignatureReader(signature).acceptType(this);
		}
	}

	private String getGroupKey(String name)
	{
		dependencies.add(name.replace("/", "."));
		return name;
	}
}
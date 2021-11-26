// Made with Blockbench 3.9.3
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports

public static class Modeliron_golem_with_joints extends EntityModel<Entity> {
	private final ModelRenderer Whole;
	private final ModelRenderer lowerBody;
	private final ModelRenderer upperBody;
	private final ModelRenderer head;
	private final ModelRenderer leftShoulder;
	private final ModelRenderer leftWrist;
	private final ModelRenderer rightShoulder;
	private final ModelRenderer rightWrist;
	private final ModelRenderer leftThigh;
	private final ModelRenderer leftShank;
	private final ModelRenderer rightThigh;
	private final ModelRenderer rightShank;

	public Modeliron_golem_with_joints() {
		textureWidth = 128;
		textureHeight = 128;

		Whole = new ModelRenderer(this);
		Whole.setRotationPoint(0.0F, 24.0F, 0.0F);

		lowerBody = new ModelRenderer(this);
		lowerBody.setRotationPoint(0.0F, -16.0F, 0.0F);
		Whole.addChild(lowerBody);
		lowerBody.setTextureOffset(47, 0).addBox(-4.5F, -5.0F, -3.0F, 9.0F, 5.0F, 6.0F, 0.5F, false);

		upperBody = new ModelRenderer(this);
		upperBody.setRotationPoint(0.0F, -5.0F, 0.0F);
		lowerBody.addChild(upperBody);
		upperBody.setTextureOffset(0, 0).addBox(-9.0F, -12.0F, -6.0F, 18.0F, 12.0F, 11.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -10.0F, -2.0F);
		upperBody.addChild(head);
		head.setTextureOffset(0, 23).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, 0.0F, false);
		head.setTextureOffset(0, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		leftShoulder = new ModelRenderer(this);
		leftShoulder.setRotationPoint(9.0F, -10.0F, 0.0F);
		upperBody.addChild(leftShoulder);
		leftShoulder.setTextureOffset(40, 44).addBox(0.0F, -2.5F, -3.0F, 4.0F, 15.0F, 6.0F, 0.0F, false);

		leftWrist = new ModelRenderer(this);
		leftWrist.setRotationPoint(2.0F, 12.0F, 0.0F);
		leftShoulder.addChild(leftWrist);
		leftWrist.setTextureOffset(20, 44).addBox(-2.0F, 0.5F, -3.0F, 4.0F, 15.0F, 6.0F, 0.0F, false);

		rightShoulder = new ModelRenderer(this);
		rightShoulder.setRotationPoint(-10.0F, -10.0F, 0.0F);
		upperBody.addChild(rightShoulder);
		rightShoulder.setTextureOffset(0, 41).addBox(-3.0F, -2.5F, -3.0F, 4.0F, 15.0F, 6.0F, 0.0F, false);

		rightWrist = new ModelRenderer(this);
		rightWrist.setRotationPoint(-1.0F, 12.0F, 0.0F);
		rightShoulder.addChild(rightWrist);
		rightWrist.setTextureOffset(32, 23).addBox(-2.0F, 0.5F, -3.0F, 4.0F, 15.0F, 6.0F, 0.0F, false);

		leftThigh = new ModelRenderer(this);
		leftThigh.setRotationPoint(4.0F, -13.0F, 0.0F);
		Whole.addChild(leftThigh);
		leftThigh.setTextureOffset(0, 62).addBox(-2.5F, -3.0F, -3.0F, 6.0F, 8.0F, 5.0F, 0.0F, false);

		leftShank = new ModelRenderer(this);
		leftShank.setRotationPoint(1.0F, 5.0F, 0.0F);
		leftThigh.addChild(leftShank);
		leftShank.setTextureOffset(60, 49).addBox(-3.5F, 0.0F, -3.0F, 6.0F, 8.0F, 5.0F, 0.0F, false);

		rightThigh = new ModelRenderer(this);
		rightThigh.setRotationPoint(-4.0F, -13.0F, 0.0F);
		Whole.addChild(rightThigh);
		rightThigh.setTextureOffset(54, 36).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 8.0F, 5.0F, 0.0F, false);

		rightShank = new ModelRenderer(this);
		rightShank.setRotationPoint(-1.0F, 5.0F, 0.0F);
		rightThigh.addChild(rightShank);
		rightShank.setTextureOffset(52, 23).addBox(-2.5F, 0.0F, -3.0F, 6.0F, 8.0F, 5.0F, 0.0F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		Whole.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
		this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
		this.rightThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
		this.rightShoulder.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
		this.leftThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
		this.leftShoulder.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
	}
}
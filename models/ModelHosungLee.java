// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports

public static class ModelHosungLee extends EntityModel<Entity> {
	private final ModelRenderer Whole;
	private final ModelRenderer lowerBody;
	private final ModelRenderer upperBody;
	private final ModelRenderer neck;
	private final ModelRenderer head;
	private final ModelRenderer rightShoulder;
	private final ModelRenderer rightWrist;
	private final ModelRenderer bat;
	private final ModelRenderer leftShoulder;
	private final ModelRenderer leftWrist;
	private final ModelRenderer rightThigh;
	private final ModelRenderer rightShank;
	private final ModelRenderer leftThigh;
	private final ModelRenderer leftShank;

	public ModelHosungLee() {
		textureWidth = 128;
		textureHeight = 128;

		Whole = new ModelRenderer(this);
		Whole.setRotationPoint(0.0F, 24.0F, 0.0F);

		lowerBody = new ModelRenderer(this);
		lowerBody.setRotationPoint(0.0F, -23.0F, 1.0F);
		Whole.addChild(lowerBody);
		lowerBody.setTextureOffset(34, 39).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 12.0F, 10.0F, 0.0F, false);
		lowerBody.setTextureOffset(0, 102).addBox(5.0F, -8.0F, -4.0F, 3.0F, 6.0F, 8.0F, 0.0F, false);
		lowerBody.setTextureOffset(45, 97).addBox(-8.0F, -8.0F, -4.0F, 3.0F, 6.0F, 8.0F, 0.0F, false);

		upperBody = new ModelRenderer(this);
		upperBody.setRotationPoint(0.0F, -8.0F, 0.0F);
		lowerBody.addChild(upperBody);
		upperBody.setTextureOffset(55, 61).addBox(6.0F, -13.0F, -6.0F, 6.0F, 9.0F, 11.0F, 0.0F, false);
		upperBody.setTextureOffset(21, 61).addBox(-12.0F, -13.0F, -6.0F, 6.0F, 9.0F, 11.0F, 0.0F, false);
		upperBody.setTextureOffset(96, 34).addBox(-9.0F, -4.0F, -5.0F, 3.0F, 4.0F, 10.0F, 0.0F, false);
		upperBody.setTextureOffset(96, 34).addBox(6.0F, -4.0F, -5.0F, 3.0F, 4.0F, 10.0F, 0.0F, true);
		upperBody.setTextureOffset(0, 0).addBox(-6.0F, -14.0F, -7.0F, 12.0F, 14.0F, 13.0F, 0.0F, false);
		upperBody.setTextureOffset(78, 61).addBox(-5.0F, -15.0F, -4.0F, 10.0F, 1.0F, 7.0F, 0.0F, false);

		neck = new ModelRenderer(this);
		neck.setRotationPoint(0.0F, -13.0F, 0.0F);
		upperBody.addChild(neck);
		neck.setTextureOffset(44, 61).addBox(-2.5F, -4.0F, -2.5F, 5.0F, 3.0F, 4.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -4.0F, -1.0F);
		neck.addChild(head);
		head.setTextureOffset(30, 81).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		head.setTextureOffset(39, 22).addBox(-4.0F, -11.0F, -7.0F, 8.0F, 4.0F, 11.0F, 0.5F, false);

		rightShoulder = new ModelRenderer(this);
		rightShoulder.setRotationPoint(-12.0F, -9.0F, 1.0F);
		upperBody.addChild(rightShoulder);
		rightShoulder.setTextureOffset(50, 0).addBox(-8.0F, -3.0F, -4.0F, 8.0F, 14.0F, 8.0F, 0.0F, false);

		rightWrist = new ModelRenderer(this);
		rightWrist.setRotationPoint(-4.0F, 11.0F, 0.0F);
		rightShoulder.addChild(rightWrist);
		rightWrist.setTextureOffset(82, 0).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 17.0F, 5.0F, 0.0F, false);

		bat = new ModelRenderer(this);
		bat.setRotationPoint(0.0F, 14.0F, 0.0F);
		rightWrist.addChild(bat);
		bat.setTextureOffset(84, 83).addBox(-2.0F, -2.0F, -6.0F, 4.0F, 4.0F, 10.0F, 0.0F, false);
		bat.setTextureOffset(0, 27).addBox(-3.0F, -3.0F, -34.0F, 6.0F, 6.0F, 16.0F, 0.0F, false);
		bat.setTextureOffset(0, 27).addBox(-2.5F, -2.5F, -36.0F, 5.0F, 5.0F, 2.0F, 0.0F, false);
		bat.setTextureOffset(65, 25).addBox(-2.5F, -2.5F, -18.0F, 5.0F, 5.0F, 12.0F, 0.0F, false);

		leftShoulder = new ModelRenderer(this);
		leftShoulder.setRotationPoint(12.0F, -9.0F, 1.0F);
		upperBody.addChild(leftShoulder);
		leftShoulder.setTextureOffset(0, 49).addBox(0.0F, -3.0F, -4.0F, 8.0F, 14.0F, 8.0F, 0.0F, false);

		leftWrist = new ModelRenderer(this);
		leftWrist.setRotationPoint(4.0F, 11.0F, 0.0F);
		leftShoulder.addChild(leftWrist);
		leftWrist.setTextureOffset(62, 81).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 17.0F, 5.0F, 0.0F, false);

		rightThigh = new ModelRenderer(this);
		rightThigh.setRotationPoint(-4.0F, -24.0F, 1.0F);
		Whole.addChild(rightThigh);
		rightThigh.setTextureOffset(0, 81).addBox(-4.0F, 0.0F, -4.0F, 7.0F, 11.0F, 8.0F, 0.0F, false);

		rightShank = new ModelRenderer(this);
		rightShank.setRotationPoint(1.0F, 11.0F, 0.0F);
		rightThigh.addChild(rightShank);
		rightShank.setTextureOffset(104, 0).addBox(-4.0F, 0.0F, -2.0F, 5.0F, 11.0F, 5.0F, 0.0F, false);
		rightShank.setTextureOffset(87, 22).addBox(-4.0F, 11.0F, -7.0F, 5.0F, 2.0F, 10.0F, 0.0F, false);

		leftThigh = new ModelRenderer(this);
		leftThigh.setRotationPoint(4.0F, -24.0F, 1.0F);
		Whole.addChild(leftThigh);
		leftThigh.setTextureOffset(74, 42).addBox(-3.0F, 0.0F, -4.0F, 7.0F, 11.0F, 8.0F, 0.0F, false);

		leftShank = new ModelRenderer(this);
		leftShank.setRotationPoint(-1.0F, 11.0F, 0.0F);
		leftThigh.addChild(leftShank);
		leftShank.setTextureOffset(84, 97).addBox(-1.0F, 0.0F, -2.0F, 5.0F, 11.0F, 5.0F, 0.0F, false);
		leftShank.setTextureOffset(79, 71).addBox(-1.0F, 11.0F, -7.0F, 5.0F, 2.0F, 10.0F, 0.0F, false);
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
		this.lowerBody.rotateAngleZ = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
	}
}
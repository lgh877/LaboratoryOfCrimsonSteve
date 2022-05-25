// Made with Blockbench 4.2.4
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports

public static class ModelChainAnimationTestMob extends EntityModel<Entity> {
	private final ModelRenderer whole;
	private final ModelRenderer body;
	private final ModelRenderer neck1;
	private final ModelRenderer neck2;
	private final ModelRenderer neck3;
	private final ModelRenderer neck4;
	private final ModelRenderer neck5;
	private final ModelRenderer neck6;
	private final ModelRenderer neck7;
	private final ModelRenderer head;
	private final ModelRenderer leftRib;
	private final ModelRenderer rightRib;
	private final ModelRenderer leftArm;
	private final ModelRenderer rightArm;
	private final ModelRenderer leftLeg;
	private final ModelRenderer rightLeg;

	public ModelChainAnimationTestMob() {
		textureWidth = 128;
		textureHeight = 128;

		whole = new ModelRenderer(this);
		whole.setRotationPoint(0.0F, 24.0F, 0.0F);

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, -9.0F, 0.0F);
		whole.addChild(body);
		body.setTextureOffset(0, 0).addBox(-10.0F, -24.0F, -7.0F, 20.0F, 24.0F, 14.0F, 0.0F, false);

		neck1 = new ModelRenderer(this);
		neck1.setRotationPoint(0.0F, -23.0F, 0.0F);
		body.addChild(neck1);
		neck1.setTextureOffset(74, 82).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		neck2 = new ModelRenderer(this);
		neck2.setRotationPoint(0.0F, -8.0F, 0.0F);
		neck1.addChild(neck2);
		neck2.setTextureOffset(74, 82).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		neck3 = new ModelRenderer(this);
		neck3.setRotationPoint(0.0F, -8.0F, 0.0F);
		neck2.addChild(neck3);
		neck3.setTextureOffset(74, 82).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		neck4 = new ModelRenderer(this);
		neck4.setRotationPoint(0.0F, -8.0F, 0.0F);
		neck3.addChild(neck4);
		neck4.setTextureOffset(74, 82).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		neck5 = new ModelRenderer(this);
		neck5.setRotationPoint(0.0F, -8.0F, 0.0F);
		neck4.addChild(neck5);
		neck5.setTextureOffset(74, 82).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		neck6 = new ModelRenderer(this);
		neck6.setRotationPoint(0.0F, -8.0F, 0.0F);
		neck5.addChild(neck6);
		neck6.setTextureOffset(74, 82).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		neck7 = new ModelRenderer(this);
		neck7.setRotationPoint(0.0F, -8.0F, 0.0F);
		neck6.addChild(neck7);
		neck7.setTextureOffset(74, 82).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -8.0F, 0.0F);
		neck7.addChild(head);
		head.setTextureOffset(0, 38).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);

		leftRib = new ModelRenderer(this);
		leftRib.setRotationPoint(7.0F, -13.0F, -7.0F);
		body.addChild(leftRib);
		leftRib.setTextureOffset(108, 0).addBox(-7.0F, -11.0F, -0.1F, 10.0F, 24.0F, 0.0F, 0.0F, false);

		rightRib = new ModelRenderer(this);
		rightRib.setRotationPoint(-7.0F, -13.0F, -7.0F);
		body.addChild(rightRib);
		rightRib.setTextureOffset(108, 0).addBox(-3.0F, -11.0F, -0.1F, 10.0F, 24.0F, 0.0F, 0.0F, true);

		leftArm = new ModelRenderer(this);
		leftArm.setRotationPoint(13.0F, -21.0F, 0.0F);
		body.addChild(leftArm);
		leftArm.setTextureOffset(64, 32).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 20.0F, 6.0F, 0.0F, false);

		rightArm = new ModelRenderer(this);
		rightArm.setRotationPoint(-13.0F, -21.0F, 0.0F);
		body.addChild(rightArm);
		rightArm.setTextureOffset(64, 32).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 20.0F, 6.0F, 0.0F, false);

		leftLeg = new ModelRenderer(this);
		leftLeg.setRotationPoint(6.0F, -9.0F, 0.0F);
		whole.addChild(leftLeg);
		leftLeg.setTextureOffset(0, 87).addBox(-4.0F, 0.0F, -3.0F, 7.0F, 9.0F, 7.0F, 0.0F, false);

		rightLeg = new ModelRenderer(this);
		rightLeg.setRotationPoint(-6.0F, -9.0F, 0.0F);
		whole.addChild(rightLeg);
		rightLeg.setTextureOffset(0, 87).addBox(-3.0F, 0.0F, -3.0F, 7.0F, 9.0F, 7.0F, 0.0F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		whole.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
		this.rightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
		this.leftLeg.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
		this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
		this.rightLeg.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
		this.leftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
	}
}
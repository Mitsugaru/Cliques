package com.mitsugaru.Cliques.permissions;

public enum PermissionNode
{
	CLIQUE_CREATE(".clique.create"),
	//Admin
	ADMIN_RELOAD(".admin.reload");

	private static final String prefix = "Cliques";
	private String node;

	private PermissionNode(String node)
	{
		this.node = prefix + node;
	}

	public String getNode()
	{
		return node;
	}
}

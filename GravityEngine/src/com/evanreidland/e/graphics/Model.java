package com.evanreidland.e.graphics;

import com.evanreidland.e.Resource;
import com.evanreidland.e.Vector3;

public class Model  {
	public static enum ModelType {
		RenderList,
		Buffer
	}
	public static abstract class ModelTypeClass {
		public abstract void passData();
	}
	private class RenderListModel extends ModelTypeClass {
		RenderList renderList;
		public void passData() {
			graphics.putTranslation(pos, scale, angle);
			renderList.Render();
			graphics.endTranslation();
		}
		public RenderListModel(RenderList renderList) {
			this.renderList = renderList;
		}
	}
	
	private class BufferModel extends ModelTypeClass {
		GraphicsData data;
		public void passData() {
			data.pos.setAs(pos);
			data.angle.setAs(angle);
			data.size.setAs(scale);
			data.passData();
		}
		public BufferModel(GraphicsData data) {
			this.data = data;
		}
	}
	
	public Resource tex;
	public Vector3 pos, scale, angle;
	
	public ModelTypeClass modelType;
	
	public void setData(RenderList rlist) {
		modelType = new RenderListModel(rlist);
	}
	public void setData(GraphicsData data) {
		modelType = new BufferModel(data);
	}
	
	public void setRenderListData(TriList triList) {
		RenderList rlist = graphics.newRenderList();
		rlist.Begin();
		rlist.addTriList(triList);
		rlist.End();
		setData(rlist);
	}
	
	public void setBufferData(TriList triList) {
		GraphicsData data = graphics.newData();
		data.addTriList(triList);
		setData(data);
	}
	
	public void setData(TriList triList, ModelType type) {
		if ( type == ModelType.RenderList ) {
			setRenderListData(triList);
		} else if ( type == ModelType.Buffer ) {
			setBufferData(triList);
		}
	}
	
	public void Render() {
		graphics.setTexture(tex);
		if ( modelType != null ) modelType.passData();
	}
	
	public Model() {
		pos = Vector3.Zero();
		angle = Vector3.Zero();
		scale = new Vector3(1, 1, 1);
		modelType = null;
	}
}

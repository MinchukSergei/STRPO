<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal-thumb" ng-show="imageThumbIsVisible" ng-click="hideThumb()">
    <div class="modal-content">
        <div class="img-wrap">
                                <span class="delete-image" ng-click="deleteImageByName(currentThumbImage)"
                                      ng-show="isOwnGallery">Delete</span>
            <img ng-src="{{currentThumbImage | imagePrefix}}">
        </div>

    </div>
</div>
<div class="thumbnail-box">
    <div class="thumb-part" ng-repeat="image in galleryImages">
        <img ng-src="{{image.imageName | imagePrefix}}" ng-click="showThumb(image.imageName)">
    </div>
    <div class="thumb-part" ng-show="isOwnGallery">
        <div class="new-thumb">
            <div class="icon-plus"></div>
            <div class="fileupload">
                <input type='file' title="Add image"
                       onchange="angular.element(this).scope().addImageToGallery(this)"/>
            </div>
        </div>
    </div>
</div>
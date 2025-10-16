import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import * as z from "zod"
import { Button } from "@/shared/ui/button"
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormMessage,
} from "@/shared/ui/form"
import { Input } from "@/shared/ui/input"
import { Textarea } from "@/shared/ui/textarea"

import { useUser } from "@/features/user/UserProvider";
import { useState } from "react"
import { postApi } from "../api/postApi";

const formSchema = z.object({
    content: z.string().max(280, "Post content must be at most 280 characters"),
    productId: z.number().optional(),
    files: z.array(z.instanceof(File)).optional()
});

export default function PostCreate() {
    const {user} = useUser();

    const [previewImages, setPreviewImages] = useState<string[]>([]);

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            content: "",
            productId: undefined,
            files: [],
        }
    })

    if (!user.artistId) {
        return (
            <div className="w-5/6 mx-auto p-4 border">
                <p className="text-red-500">
                    Only artists can create posts. Please register as an artist to post.
                </p>
            </div>
        );
    }

    async function onSubmit(values: z.infer<typeof formSchema>) {
        try {
          await postApi.createPost({
            artistId: user.artistId,
            content: values.content,
            productId: values.productId ?? null,
            files: values.files,
          });
      
          console.log("Post created successfully");
        } catch (error) {
          console.error("Error creating post", error);
        }
      }
    return (
        <Form {...form} >
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 py-8 w-5/6 mx-auto p-4 border rounded-lg shadow-md bg-white">
                <FormField
                    control={form.control}
                    name="content"
                    render={({ field }) => (
                    <FormItem>
                        <FormControl>
                            <Textarea 
                                placeholder="What's up with your new work?"
                                className="h-20"
                                {...field} 
                            />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                    )}
                />
               <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                    {previewImages.map((src, index) => (
                        <div key={index} className="relative w-full h-32 md:h-40 lg:h-48">
                            <img 
                                src={src} 
                                alt={`Preview ${index + 1}`} 
                                className="rounded-md border w-full h-full object-cover"
                            />
                        </div>
                    ))}
                </div>
                <FormField
                    control={form.control}
                    name="files"
                    render={({ field }) => (
                        <FormItem>
                        <FormControl>
                            <Input
                                type="file"
                                accept="image/*"
                                multiple
                                onChange={(e) => {
                                    const selectedFiles = e.target.files ? Array.from(e.target.files) : [];

                                    const existingFiles = field.value || [];
                                    if (existingFiles.length + selectedFiles.length > 4) {
                                        alert("You can upload a maximum of 4 images.");
                                        return;
                                    }
                                    const updatedFiles = [...(field.value || []), ...selectedFiles];
            
                                    field.onChange(updatedFiles); 

                                    const imagePreviews = updatedFiles.map(file => URL.createObjectURL(file));
                                    setPreviewImages(imagePreviews);
                                }}
                            />
                        </FormControl>
                        <FormDescription>Upload images for your post.</FormDescription>
                        <FormMessage />
                        </FormItem>
                    )}
                />
            <Button type="submit">Post</Button>
            </form>
        </Form>
    )
}